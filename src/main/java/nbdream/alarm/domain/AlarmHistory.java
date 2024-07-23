package nbdream.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AlarmHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private String title;

    private String content;

    private boolean checked;

    private Long targetId;

    public AlarmHistory(final Alarm alarm, final AlarmType alarmType, final String title, final String content, final boolean checked, final Long targetId) {
        this.alarm = alarm;
        this.alarmType = alarmType;
        this.title = title;
        this.content = content;
        this.checked = checked;
        this.targetId = targetId;
    }

    public void alarmCheck() {
        this.checked = true;
    }
}
