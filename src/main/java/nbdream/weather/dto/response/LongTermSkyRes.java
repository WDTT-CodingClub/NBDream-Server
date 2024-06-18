package nbdream.weather.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nbdream.weather.domain.Sky;

@Getter
@NoArgsConstructor
@Setter
public class LongTermSkyRes {
    private String date;
    private Sky sky;

    public LongTermSkyRes(String date) {
        this.date = date;
    }
}
