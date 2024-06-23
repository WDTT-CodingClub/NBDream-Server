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

    private final FarmRepository farmRepository;
    private final LandElementsRepository landElementsRepository;
    private final KakaoAsyncService kakaoAsyncService;
    private final RestTemplate restTemplate;

    @Value("${soil.api.url}")
    private String soilApiUrl;

    @Value("${soil.api.key}")
    private String soilApiKey;


    //유저의 토양 정보가 있으면 반환, 없으면 저장 후 반환
    public GetLandElementResDto getLandElementsByBjd(Long memberId) {
        Farm farm = farmRepository.findByMemberId(memberId)
                .orElseThrow(FarmNotFoundException::new);
        if (farm.getLocation() == null || farm.getLocation().getBjdCode() == null || farm.getLocation().getBjdCode().isBlank() ||
                farm.getLocation().getLatitude() == 0 || farm.getLocation().getLongitude() == 0) {
            throw new CoordinatesNotFoundException();
        }

        LandElements landElements = getOrFetchLandElements(farm);

        return new GetLandElementResDto().updateResDto(landElements);
    }

    private LandElements getOrFetchLandElements(Farm farm) {
        LandElements landElements = farm.getLandElements();
        if (landElements == null) {
            landElements = getNewLandElementsFromOpenApi(farm);
            landElementsRepository.save(landElements);
            farm.updateLandElements(landElements);
            farmRepository.save(farm);
        }
        return landElements;
    }


    // 유저의 법정동 코드로 토양 리스트를 받아와서, 가장 가까운 위치의 토양 성분을 반환
    private LandElements getNewLandElementsFromOpenApi(Farm farm) {
        List<ItemBjd> soilDataList = fetchSoilDataFromApiByBjd(farm.getLocation().getBjdCode(), "List");
        //유저와 토양 리스트의 위치 비교
        Coordinates userCoordinates = new Coordinates(farm.getLocation().getLatitude(), farm.getLocation().getLongitude());
        Map<Integer, Coordinates> coordinatesMap = getCoordinatesByAddressFromKakao(soilDataList);
        int num = findNearestLocation(userCoordinates, coordinatesMap);

        ItemBjd closestData = soilDataList.stream()
                .filter(soilData -> num == soilData.getNo())
                .findFirst().orElseThrow(ClosestSoilDataInternalServerErrorException::new);
        return new LandElements(
                closestData.getAcid(),
                closestData.getVldpha(),
                closestData.getVldsia(),
                closestData.getOm(),
                closestData.getPosifertMg(),
                closestData.getPosifertK(),
                closestData.getPosifertCa(),
                closestData.getSelc()
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


    //농장 주소 수정시 실행되어야 할 메서드
    public void updateLandElements(Long memberId){
        Farm farm = farmRepository.findByMemberId(memberId).orElseThrow(FarmNotFoundException::new);
        LandElements landElements = landElementsRepository.findById(farm.getLandElements().getId())
                .orElseThrow(LandElementsNotFoundException::new);
        LandElements newLandElements = getNewLandElementsFromOpenApi(farm);
        landElements.update(newLandElements);
        landElementsRepository.save(landElements);
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

