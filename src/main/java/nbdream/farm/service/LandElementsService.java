package nbdream.farm.service;

import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Farm;
import nbdream.farm.exception.*;
import nbdream.farm.repository.FarmRepository;
import nbdream.farm.domain.LandElements;
import nbdream.farm.repository.LandElementsRepository;
import nbdream.farm.service.dto.LandElements.GetLandElementResDto;
import nbdream.farm.service.dto.LandElements.Item;
import nbdream.farm.service.dto.LandElements.SoilDataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.net.URL;
import java.util.List;

import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class LandElementsService {

    private final FarmRepository farmRepository;
    private final LandElementsRepository landElementsRepository;

    //토양검정
    private final RestTemplate soilRestTemplate;

    @Value("${soil.api.url}")
    private String soilApiUrl;

    @Value("${soil.api.key}")
    private String soilApiKey;



    public GetLandElementResDto getLandElements(Long memberId) {
        Farm farm = farmRepository.findByMemberId(memberId)
                .orElseThrow(FarmNotFoundException::new);
        if (farm.getLocation() == null || farm.getLocation().getPnuCode() == null || farm.getLocation().getPnuCode().isEmpty()) {
            throw new PnuNotFoundException();
        }

        LandElements landElements = farm.getLandElements();
        GetLandElementResDto response = new GetLandElementResDto();
        // 토양 정보가 없으면 받아와서 저장
        if (landElements == null) {
            landElements = fetchSoilDataFromApi(farm.getLocation().getPnuCode());
            landElementsRepository.save(landElements);
            farm.updateLandElements(landElements);
            farmRepository.save(farm);
        }
        response.updateResDto(landElements);
        return response;
    }

    //농장 주소 수정시 실행되어야 할 메서드
    public void fetchLandElements(Farm farm){
        LandElements landElements = landElementsRepository.findById(farm.getLandElements().getId())
                .orElseThrow(LandElementsNotFoundException::new);
        LandElements newLandElements = fetchSoilDataFromApi(farm.getLocation().getPnuCode());
        landElements.update(newLandElements);
        landElementsRepository.save(landElements);
    }



    //토양검정
    private LandElements fetchSoilDataFromApi(String pnuCode) {
        try {
            StringBuilder urlBuilder = new StringBuilder(soilApiUrl);
            urlBuilder.append("?serviceKey" +  "=" + soilApiKey);
            urlBuilder.append("&PNU_Code"  + "=" + pnuCode);
            URL url = new URL(urlBuilder.toString());

            String xmlResponse = soilRestTemplate.getForObject(url.toURI(), String.class);
            return parseSoilData(xmlResponse);
        } catch (Exception e) {
            throw new FetchApiInternalServerErrorException();
        }
    }

    private LandElements parseSoilData(String xmlResponse) {
        try{
            XmlMapper xmlMapper = new XmlMapper();
            SoilDataResponse response = xmlMapper.readValue(xmlResponse, SoilDataResponse.class);
            List<Item> items = response.getBody().getItems();

            if(items != null && !(items.isEmpty())){
                Item item = items.get(0);
                LandElements landElements = new LandElements(item.getAcid(), item.getVldpha(), item.getVldsia(), item.getOm(), item.getPosifertMg(), item.getPosifertK(), item.getPosifertCa(), item.getSelc());
                return landElements;
            }
        }catch (Exception e){
            throw new ParsingInternalServerErrorException();
        }
        return null;
    }

}
