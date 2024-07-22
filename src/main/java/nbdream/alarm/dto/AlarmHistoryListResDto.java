package nbdream.alarm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.alarm.domain.AlarmHistory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AlarmHistoryListResDto {
    List<AlarmHistoryResDto> alarmHistoryList;

    public static AlarmHistoryListResDto from(List<AlarmHistory> alarmHistories) {
        List<AlarmHistoryResDto> list = alarmHistories.stream()
                .map(alarmHistory -> new AlarmHistoryResDto(
                        alarmHistory.getId(),
                        alarmHistory.getAlarmType().getValue(),
                        alarmHistory.getTitle(),
                        alarmHistory.getContent(),
                        alarmHistory.isChecked(),
                        alarmHistory.getCreatedDate()
                ))
                .collect(Collectors.toList());

        return new AlarmHistoryListResDto(list);
    }
}

@Getter
@NoArgsConstructor
class AlarmHistoryResDto {
    private Long id;
    private String alarmType;
    private String title;
    private String content;
    private boolean checked;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    public AlarmHistoryResDto(Long id, String alarmType, String title, String content, boolean checked, LocalDateTime createdDate) {
        this.id = id;
        this.alarmType = alarmType;
        this.title = title;
        this.content = content;
        this.checked = checked;
        this.createdDate = ZonedDateTime.of(createdDate, ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }

}
