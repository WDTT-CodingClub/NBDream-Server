package nbdream.farm.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtil {

    //Uri에 한글이 들어갈 경우 인코딩 해주는 메서드
    public static String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
