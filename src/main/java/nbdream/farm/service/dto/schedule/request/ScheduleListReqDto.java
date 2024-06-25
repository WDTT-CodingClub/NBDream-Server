package nbdream.farm.service.dto.schedule.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.accountBook.exception.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ScheduleListReqDto {
    private String crop;
    private int year;
    private int month;

    public LocalDate createStartDate(){
        return LocalDate.of(year, month, 1);
    }
    public LocalDate createEndDate(){
        LocalDate startDate = LocalDate.of(year, month, 1);
        return startDate.withDayOfMonth(startDate.lengthOfMonth());
    }
}
