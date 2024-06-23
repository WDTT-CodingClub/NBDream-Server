package nbdream.farm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbdream.farm.exception.KakaoLocalServiceErrorException;
import nbdream.farm.service.dto.workSchedule.kakaoResponse.KakaoResponse;
import nbdream.farm.util.Coordinates;
import nbdream.farm.util.UrlUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoAddressService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao-local.api.url}")
    private String kakaoLocalUrl;

    @Value("${kakao-local.api.key}")
    private String kakaoLocalAPIKey;



    // 매개변수 : 주소
    // 리턴 : 위도 경도
    public Coordinates kakaoAddressSearch(String query) {
        try{
            //URI
            String encodedQuery = UrlUtil.encodeParam(query);
            StringBuilder urlBuilder = new StringBuilder(kakaoLocalUrl);
            urlBuilder.append(".json");
            urlBuilder.append("?query=").append(encodedQuery);
            URL url = new URL(urlBuilder.toString());
            //헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK" + " " + kakaoLocalAPIKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url.toURI(), HttpMethod.GET, entity, String.class);
            KakaoResponse kakaoResponse = objectMapper.readValue(response.getBody(), KakaoResponse.class);
            return new Coordinates(kakaoResponse.getDocuments().get(0).getY(), kakaoResponse.getDocuments().get(0).getX());
        } catch (Exception e){
            throw new KakaoLocalServiceErrorException();
        }
    }
}
