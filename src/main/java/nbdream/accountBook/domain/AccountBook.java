package nbdream.accountBook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.accountBook.service.dto.GetAccountBookResDto;
import nbdream.common.entity.BaseEntity;
import nbdream.member.domain.Member;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class AccountBook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;


    // 장부 로직 관련 메서드를 여기에
    // 총 수입
    public static Long getTotalRevenue(List<GetAccountBookResDto> items){
        Long totalRevenue = 0L;
        for (GetAccountBookResDto dto : items) {
            if(!(dto.getRevenue() == null)){
                totalRevenue += dto.getRevenue();
            }
        }
        return totalRevenue;
    }

    // 총 지출
    public static Long getTotalExpense(List<GetAccountBookResDto> items){
        Long totalExpense = 0L;
        for (GetAccountBookResDto dto : items) {
            if(!(dto.getExpense() == null)){
                totalExpense += dto.getExpense();
            }
        }
        return -totalExpense;
    }
}
