package nbdream.farm.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.FarmWorkSchedule;
import nbdream.farm.exception.FarmWorkScheduleAlreadyExistsException;
import nbdream.farm.exception.FetchApiInternalServerErrorException;
import nbdream.farm.exception.ParsingInternalServerErrorException;
import nbdream.farm.repository.FarmWorkScheduleRepository;
import nbdream.farm.service.dto.workSchedule.codeList.CodeItem;
import nbdream.farm.service.dto.workSchedule.codeList.CodeListResponse;
import nbdream.farm.service.dto.workSchedule.detailList.DetailItem;
import nbdream.farm.service.dto.workSchedule.detailList.DetailListResponse;
import nbdream.farm.service.dto.workSchedule.workList.WorkItem;
import nbdream.farm.service.dto.workSchedule.workList.WorkListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

import static nbdream.farm.service.dto.workSchedule.RequiredCrop.containsValue;

@Service
@RequiredArgsConstructor
public class FarmWorkScheduleService {

    private final FarmWorkScheduleRepository farmWorkScheduleRepository;
    private final RestTemplate restTemplate;

    @Value("${farm-work.api.url}")
    private String apiUrl;
    @Value("${farm-work.api.key}")
    private String apiKey;

    public void saveWorkSchedule() {
        if (farmWorkScheduleRepository.count() > 0) {
            throw new FarmWorkScheduleAlreadyExistsException();
        }

        List<CodeItem> codeItems = fetchWorkScheduleCodeList();
        for (CodeItem codeItem : codeItems) {
            List<WorkItem> workItems = fetchWorkScheduleList(codeItem.getKidofcomdtySeCode());
            for (WorkItem workItem : workItems) {
                // 하드코딩 1
                if (!containsValue(workItem.getSj())) {
                    continue;
                }
                List<DetailItem> detailItems = fetchWorkScheduleDetail(workItem.getCntntsNo());
                if (detailItems == null) {
                    continue;
                }
                for (DetailItem detailItem : detailItems) {
                    saveFarmWorkSchedule(detailItem);
                }
            }
        }
    }

    public List<CodeItem> fetchWorkScheduleCodeList() {
        return fetchData("/workScheduleGrpList", null, CodeListResponse.class).getBody().getItems().getItem();
    }

    private List<WorkItem> fetchWorkScheduleList(String code) {
        return fetchData("/workScheduleLst", "kidofcomdtySeCode=" + code, WorkListResponse.class).getBody().getItems().getItem();
    }

    private List<DetailItem> fetchWorkScheduleDetail(String contentsNum) {
        return fetchData("/workScheduleEraInfoJsonLst", "cntntsNo=" + contentsNum, DetailListResponse.class).getBody().getItems().getItem();
    }

    private <T> T fetchData(String path, String query, Class<T> responseType) {
        try {
            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            urlBuilder.append(path);
            urlBuilder.append("?apiKey=").append(apiKey);
            if (query != null && !query.isEmpty()) {
                urlBuilder.append("&").append(query);
            }
            URL url = new URL(urlBuilder.toString());
            String xmlResponse = restTemplate.getForObject(url.toURI(), String.class);
            return parseResponse(xmlResponse, responseType);
        } catch (Exception e) {
            throw new FetchApiInternalServerErrorException();
        }
    }

    private <T> T parseResponse(String xmlResponse, Class<T> responseType) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(xmlResponse, responseType);
        } catch (Exception e) {
            throw new ParsingInternalServerErrorException();
        }
    }

    private void saveFarmWorkSchedule(DetailItem detailItem) {
        String crop = detailItem.getFarmWorkFlag();
        // 하드코딩 2
        switch (detailItem.getFarmWorkFlag()) {
            case "기계이앙재배":
                crop = "벼";
                break;
            case "고추(보통재배)":
                crop = "고추";
                break;
            case "딸기(촉성재배)":
                crop = "딸기";
                break;
        }

        farmWorkScheduleRepository.save(
                new FarmWorkSchedule(
                        detailItem.getKidofcomdtySeCodeNm(),
                        crop,
                        detailItem.getBeginEra(),
                        detailItem.getEndEra(),
                        detailItem.getInfoSeCodeNm(),
                        detailItem.getOpertNm(),
                        detailItem.getVodUrl(),
                        Integer.parseInt(detailItem.getBeginMon()),
                        Integer.parseInt(detailItem.getEndMon()),
                        Integer.parseInt(detailItem.getReqreMonth())
                ));
    }
}
