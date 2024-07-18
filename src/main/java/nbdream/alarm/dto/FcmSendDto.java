package nbdream.alarm.dto;


import lombok.*;
import nbdream.alarm.domain.AlarmType;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendDto {
    private String token;
    private String title;
    private String body;
    private Long targetId;
    private AlarmType alarmType;

    @Builder(toBuilder = true)
    public FcmSendDto(String token, String title, String body, Long targetId, AlarmType alarmType) {
        this.token = token;
        this.title = title;
        this.body = body;
        this.targetId = targetId;
        this.alarmType = alarmType;
    }
}
