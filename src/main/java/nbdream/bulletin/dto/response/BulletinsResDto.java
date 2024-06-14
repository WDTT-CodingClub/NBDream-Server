package nbdream.bulletin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulletinsResDto {
    private List<BulletinResDto> bulletins;
    private boolean hasNext;
}
