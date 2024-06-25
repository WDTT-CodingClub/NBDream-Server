package nbdream.calendar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.farm.domain.FarmingDiary;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farming_diary_id")
    private FarmingDiary farmingDiary;

    private String dateKind;

    private String localDate;

    private String dateName;

    private String isHoliday;

    @Builder
    public Holiday(FarmingDiary farmingDiary, String dateKind, String localDate, String dateName, String isHoliday) {
        this.farmingDiary = farmingDiary;
        this.dateKind = dateKind;
        this.localDate = localDate;
        this.dateName = dateName;
        this.isHoliday = isHoliday;

    }

}
