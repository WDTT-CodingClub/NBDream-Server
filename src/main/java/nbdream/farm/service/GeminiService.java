package nbdream.farm.service;


import lombok.RequiredArgsConstructor;
import nbdream.accountBook.exception.CategoryNotFoundException;
import nbdream.farm.domain.Crop;
import nbdream.farm.domain.Farm;
import nbdream.farm.domain.LandElements;
import nbdream.farm.exception.FetchApiInternalServerErrorException;
import nbdream.farm.exception.LandElementsNotFoundException;
import nbdream.farm.repository.CropRepository;
import nbdream.farm.repository.FarmRepository;
import nbdream.farm.repository.LandElementsRepository;
import nbdream.farm.service.dto.gemini.ChatRequest;
import nbdream.farm.service.dto.gemini.ChatResponse;
import nbdream.farm.service.dto.gemini.PostAiChatReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeminiService {
    private final FarmRepository farmRepository;
    private final LandElementsRepository landElementsRepository;
    private final CropRepository cropRepository;
    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getContents(PostAiChatReqDto reqDto, Long memberId) {
        List<Crop> cropList = cropRepository.findAll();
        if(cropList == null || cropList.isEmpty()){
            throw new CategoryNotFoundException();
        }
        LandElements landElements = getLandElementsByMemberId(memberId)
                .orElseThrow(LandElementsNotFoundException::new);

        String validQuestion = validQuestion(reqDto.getQuestion(), memberId, cropList, landElements);
        boolean isValid = isValid(reqDto.getQuestion(), cropList);

        if(!isValid){
            return validQuestion;
        }

        String requestUrl = apiUrl + "?key=" + geminiApiKey;
        ChatRequest request = new ChatRequest(validQuestion);

        //헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(request, headers);

        ChatResponse response = restTemplate.postForObject(requestUrl, requestEntity, ChatResponse.class);

        if (response != null && !response.getCandidates().isEmpty()) {
            return response.getCandidates().get(0).getContent().getParts().get(0).getText();
        } else {
            throw new FetchApiInternalServerErrorException();
        }
    }

    //Open API 요청을 할지 검증
    private boolean isValid(String question, List<Crop> cropList) {
        if(question.equals("1")){
            return true;
        }else if(question.equals("2")){
            return false;
        }else if(isCrop(question, cropList)){
            return true;
        }else{
            return false;
        }
    }

    //질문을 검증
    private String validQuestion(String question, Long memberId, List<Crop> cropList, LandElements landElements) {

        if(question.equals("1")){
            return "작물에 관해서 토지 성분 분석 결과가 궁굼해요. 토양 정보 : " + landElements.toStringForAIChat();
        }else if(question.equals("2")){
            return "어떤 작물이 궁굼하신가요? \n" +
                    listToString(cropList) + "\n" +
                    "(작물 이름만 입력해 주세요)";
        }else if(isCrop(question, cropList)){
            return "작물인 " + question + "에 대한 토양 적합성이 궁굼해. 토양 정보 : " + landElements.toStringForAIChat();
        }
        else{
            return "저에게 여쭤보고 싶은게 있으신가요?\n" +
                    "1. 토지 성분 분석 결과가 궁굼해요\n" +
                    "2. 작물별 토양 적합성이 궁굼해요\n" +
                    " (숫자로 입력해 주세요)";
        }
    }

    public static String listToString(List<Crop> cropList) {
        String str = "(가능한 작물 종류 : ";

        for(Crop crop : cropList){
            str += crop.getName() + " ";
        }
        str += ")";
        return str;
    }

    public static boolean isCrop(String question, List<Crop> cropList) {
        for(Crop crop : cropList){
            if (crop.getName().equals(question)) {
                return true;
            }
        }
        return false;
    }

    public Optional<LandElements> getLandElementsByMemberId(Long memberId) {
        Optional<Farm> farmOptional = farmRepository.findByMemberId(memberId);
        return farmOptional.map(Farm::getLandElements);
    }
}
