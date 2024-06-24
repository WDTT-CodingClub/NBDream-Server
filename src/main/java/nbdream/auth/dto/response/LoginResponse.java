package nbdream.auth.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private boolean alreadyExistMember;
    private TokenResponse tokenResponse;
    private Long memberId;
}
