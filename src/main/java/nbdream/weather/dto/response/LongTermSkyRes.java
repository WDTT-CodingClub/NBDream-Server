package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LongTermSkyRes {
    private String date;
    private String sky;

    public LongTermSkyRes(String date) {
        this.date = date;
    }
}
