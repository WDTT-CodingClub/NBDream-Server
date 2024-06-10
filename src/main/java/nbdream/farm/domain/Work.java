package nbdream.farm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Work extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farming_diary_id")
    private FarmingDiary farmingDiary;

    @Enumerated(EnumType.STRING)
    private WorkCategory workCategory;

    private String content;

    public Work(final FarmingDiary farmingDiary, final WorkCategory workCategory, final String content) {
        this.farmingDiary = farmingDiary;
        this.workCategory = workCategory;
        this.content = content;
    }
}
