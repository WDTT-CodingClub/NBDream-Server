package nbdream.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nbdream.alarm.domain.Alarm;
import nbdream.farm.domain.Schedule;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@ToString
public class AlarmScheduleDto {
    private final Alarm alarm;
    private final Schedule schedule;

    // ex) 5월 21일(화)에 예정된 일정이 있어요
    public String createScheduleAlarmTitle() {
        LocalDateTime dateTime = this.getSchedule().getAlarmDateTime();
        String[] dayOfWeekKorean = {"월", "화", "수", "목", "금", "토", "일"};

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M월 d일");
        String formattedDate = dateTime.format(outputFormatter);

        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        String dayOfWeekStr = dayOfWeekKorean[dayOfWeek.getValue() - 1];

        return formattedDate + "(" + dayOfWeekStr + ")" + "에 예정된 일정이 있어요";
    }

    // ex) 감자 캐는 날 24.05.21
    public String createScheduleAlarmBody() {
        LocalDateTime dateTime = this.getSchedule().getAlarmDateTime();
        String scheduleTitle = this.getSchedule().getTitle();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return scheduleTitle + " " + dateTime.format(outputFormatter);
    }
}
