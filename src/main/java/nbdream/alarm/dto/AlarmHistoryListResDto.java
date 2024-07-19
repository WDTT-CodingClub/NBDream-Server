package nbdream.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.alarm.domain.AlarmHistory;
import nbdream.alarm.domain.AlarmType;

import java.util.ArrayList;
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
                        alarmHistory.isChecked()
                ))
                .collect(Collectors.toList());

        return new AlarmHistoryListResDto(list);
    }
}

@Getter
@NoArgsConstructor
@AllArgsConstructor
class AlarmHistoryResDto {
    private Long id;
    private String alarmType;
    private String title;
    private String content;
    private boolean checked;

}
