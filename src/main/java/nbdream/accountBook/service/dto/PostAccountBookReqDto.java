package nbdream.accountBook.service.dto;

import lombok.*;
import nbdream.accountBook.exception.InvalidDateFormatException;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostAccountBookReqDto {

    private Long revenue;
    private Long expense;
    private String category;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private String registerDateTime;

    public LocalDateTime getParsedRegisterDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(registerDateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
