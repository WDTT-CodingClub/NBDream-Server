package nbdream.farm.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.LandElements;
import nbdream.farm.exception.*;
import nbdream.farm.repository.FarmRepository;
import nbdream.farm.repository.LandElementsRepository;
import nbdream.farm.service.dto.LandElements.soilData.GetLandElementResDto;
import nbdream.farm.service.dto.LandElements.soilDataList.ItemBjd;
import nbdream.farm.service.dto.LandElements.soilDataList.SoilDataListResponse;
import nbdream.farm.util.Coordinates;
import nbdream.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static nbdream.farm.util.Coordinates.findNearestLocation;

@Service
@RequiredArgsConstructor
public class LandElementsService {

    private final LandElementsRepository landElementsRepository;
    private final FarmRepository farmRepository;
    private final KakaoAsyncService kakaoAsyncService;
    private final RestTemplate restTemplate;

    @Value("${soil.api.url}")
    private String soilApiUrl;

    @Value("${soil.api.key}")
    private String soilApiKey;


    //유저의 토양 정보 반환
    public GetLandElementResDto getLandElements(Long memberId) {
        final Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        LandElements landElements = farm.getLandElements();
        if(landElements == null){
            throw new LandElementsNotFoundException();
        }
        return new GetLandElementResDto().updateResDto(landElements);
    }
    
    //법정동 코드가 변경되었다면 저장 또는 업데이트
    public void saveOrUpdateLandElements(Farm farm, String bjdCode, Coordinates coordinates) {
        if(bjdCode.equals(farm.getLocation().getBjdCode())){
            return ;
        }
        LandElements landElements = farm.getLandElements();
        if (landElements == null) {
            landElements = getNewLandElementsFromOpenApi(bjdCode, coordinates);
            landElementsRepository.save(landElements);
            farm.updateLandElements(landElements);
        }else{
            updateLandElements(farm, bjdCode, coordinates);
        }
    }

    //업데이트
    private void updateLandElements(Farm farm, String bjdCode, Coordinates coordinates){
        LandElements landElements = farm.getLandElements();
        LandElements newLandElements = getNewLandElementsFromOpenApi(bjdCode, coordinates);
        landElements.update(newLandElements);
        landElementsRepository.save(landElements);
    }

    // 유저의 법정동 코드로 토양 리스트를 받아와서, 가장 가까운 위치의 토양 성분을 반환
    private LandElements getNewLandElementsFromOpenApi(String bjdCode, Coordinates coordinates) {
        List<ItemBjd> soilDataList = fetchSoilDataFromApiByBjd(bjdCode, "List");
        //유저와 토양 리스트의 위치 비교
        Map<Integer, Coordinates> coordinatesMap = getCoordinatesByAddressFromKakao(soilDataList);
        int num = findNearestLocation(coordinates, coordinatesMap);

        ItemBjd itemBjd = soilDataList.stream()
                .filter(soilData -> num == soilData.getNo())
                .findFirst().orElseThrow(ClosestSoilDataInternalServerErrorException::new);
        return new LandElements(
                itemBjd.getAcid(),
                itemBjd.getVldpha(),
                itemBjd.getVldsia(),
                itemBjd.getOm(),
                itemBjd.getPosifertMg(),
                itemBjd.getPosifertK(),
                itemBjd.getPosifertCa(),
                itemBjd.getSelc()
        );
    }

    //토양검정(BJD) Open API 요청
    private List<ItemBjd> fetchSoilDataFromApiByBjd(String bjdCode, String path) {
        try {
            StringBuilder urlBuilder = new StringBuilder(soilApiUrl);
            urlBuilder.append(path);
            urlBuilder.append("?serviceKey" +  "=" + soilApiKey);
            urlBuilder.append("&BJD_Code"  + "=" + bjdCode);
            urlBuilder.append("&Page_Size"  + "=" + "100");
            urlBuilder.append("&Page_No"  + "=" + "1");
            URL url = new URL(urlBuilder.toString());

            String xmlResponse = restTemplate.getForObject(url.toURI(), String.class);
            return removeDuplicates(parseSoilDataList(xmlResponse));
        } catch (Exception e) {
            throw new FetchApiInternalServerErrorException();
        }
    }

    // 중복 제거 (주소가 같으면 최신년도 정보만 남김)
    private List<ItemBjd> removeDuplicates(List<ItemBjd> soilList) {
        Map<String, ItemBjd> filteredMap = soilList.stream()
                .collect(Collectors.toMap(
                        ItemBjd::getPnuNm,
                        item -> item,
                        (existing, replacement) -> existing.getAnyYear() >= replacement.getAnyYear() ? existing : replacement
                ));
        return filteredMap.values().stream().collect(Collectors.toList());
    }

    //파싱(BJD)
    private List<ItemBjd> parseSoilDataList(String xmlResponse) {
        try{
            XmlMapper xmlMapper = new XmlMapper();
            SoilDataListResponse response = xmlMapper.readValue(xmlResponse, SoilDataListResponse.class);
            List<ItemBjd> items = response.getBody().getItems();
            if(items != null && !(items.isEmpty())){
                return items;
            }
        }catch (Exception e){
            throw new ParsingInternalServerErrorException();
        }
        return null;
    }

    // 주소를 위도, 경도값으로 받아오는 카카오API
    private Map<Integer, Coordinates> getCoordinatesByAddressFromKakao(List<ItemBjd> items) {
        Map<Integer, String> addressMap = new HashMap<>();
        Map<Integer, Coordinates> coordinatesMap = new ConcurrentHashMap<>();

        for (ItemBjd item : items) {
            addressMap.put(item.getNo(), item.getPnuNm());
        }
        List<CompletableFuture<Void>> futures = addressMap.entrySet().stream()
                .map(entry -> kakaoAsyncService.getCoordinatesAsync(entry.getValue())
                        .thenAccept(coordinates -> {
                            coordinatesMap.put(entry.getKey(), coordinates);
                        }))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return coordinatesMap;
    }

}

//
//
//    // 요청 : 유저ID(PNU)
//    // 응답 : 토양정보
//    public GetLandElementResDto getLandElements(Long memberId) {
//        Farm farm = farmRepository.findByMemberId(memberId)
//                .orElseThrow(FarmNotFoundException::new);
//        if (farm.getLocation() == null || farm.getLocation().getPnuCode() == null || farm.getLocation().getPnuCode().isEmpty()) {
//            throw new PnuNotFoundException();
//        }
//
//        LandElements landElements = farm.getLandElements();
//        GetLandElementResDto response = new GetLandElementResDto();
//        // 토양 정보가 없으면 받아와서 저장
//        if (landElements == null) {
//            landElements = fetchSoilDataFromApi(farm.getLocation().getPnuCode());
//            landElementsRepository.save(landElements);
//            farm.updateLandElements(landElements);
//            farmRepository.save(farm);
//        }
//        response.updateResDto(landElements);
//        return response;
//    }
//
//    //토양검정(PNU)
//    private LandElements fetchSoilDataFromApi(String pnuCode) {
//        try {
//            StringBuilder urlBuilder = new StringBuilder(soilApiUrl);
//            urlBuilder.append("?serviceKey" +  "=" + soilApiKey);
//            urlBuilder.append("&PNU_Code"  + "=" + pnuCode);
//            URL url = new URL(urlBuilder.toString());
//
//            String xmlResponse = restTemplate.getForObject(url.toURI(), String.class);
//            return parseSoilData(xmlResponse);
//        } catch (Exception e) {
//            throw new FetchApiInternalServerErrorException();
//        }
//    }
//
//    //파싱(PNU)
//    private LandElements parseSoilData(String xmlResponse) {
//        try{
//            XmlMapper xmlMapper = new XmlMapper();
//            SoilDataResponse response = xmlMapper.readValue(xmlResponse, SoilDataResponse.class);
//            List<Item> items = response.getBody().getItems();
//
//            if(items != null && !(items.isEmpty())){
//                Item item = items.get(0);
//                LandElements landElements = new LandElements(item.getAcid(), item.getVldpha(), item.getVldsia(), item.getOm(), item.getPosifertMg(), item.getPosifertK(), item.getPosifertCa(), item.getSelc());
//                return landElements;
//            }
//        }catch (Exception e){
//            throw new ParsingInternalServerErrorException();
//        }
//        return null;
//    }

