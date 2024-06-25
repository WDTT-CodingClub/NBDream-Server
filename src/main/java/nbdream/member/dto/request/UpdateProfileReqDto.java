package nbdream.member.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProfileReqDto {
    private String nickname;
    private String profileImageUrl;

    @NotNull
    private String address;
    @NotNull
    private String bjdCode;
    @NotNull
    private double longitude;
    @NotNull
    private double latitude;
    @NotNull
    private List<String> crops;
}
