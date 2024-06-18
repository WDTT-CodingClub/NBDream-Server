package nbdream.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileReqDto {
    private String nickname;
    private String profileImageUrl;
    private String address;
    private double longitude;
    private double latitude;
    private List<String> crops;
}
