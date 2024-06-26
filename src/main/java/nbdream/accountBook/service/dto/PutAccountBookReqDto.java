package nbdream.accountBook.service.dto;

import lombok.*;
import nbdream.accountBook.exception.InvalidDateFormatException;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PutAccountBookReqDto {

    private String transactionType;
    private Long amount;
    private String category;
    private String title;
    private String registerDateTime;
    private List<String> imageUrls;

    public LocalDateTime parseToDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(registerDateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
