package nbdream.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResDto {
    private Long memberId;
    private String nickname;
    private String address;
    private String profileImageUrl;
    private String bjdCode;
    private double longitude;
    private double latitude;
    private List<String> crops;
}
