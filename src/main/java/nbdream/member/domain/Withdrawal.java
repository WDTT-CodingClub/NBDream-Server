package nbdream.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Withdrawal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String reason;

    private String otherReasons;

    public Withdrawal(Long memberId, String reason, String otherReasons) {
        this.memberId = memberId;
        this.reason = reason;
        this.otherReasons = otherReasons;
    }
}
