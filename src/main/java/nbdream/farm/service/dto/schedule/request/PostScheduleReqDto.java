package nbdream.farm.service.dto.schedule.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nbdream.accountBook.exception.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PostScheduleReqDto {
    private String category;
    private String title;
    private String startDate;
    private String endDate;
    private String memo;
    private boolean alarmOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime alarmDateTime;

    public LocalDate parseStartDate() {
        return parseDate(startDate);
    }

    public LocalDate parseEndDate() {
        return parseDate(endDate);
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
