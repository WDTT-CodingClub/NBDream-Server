package nbdream.image.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import nbdream.common.entity.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("status= 'NORMAL'")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    private String imageUrl;

    public Image(final Long targetId, final String imageUrl) {
        this.targetId = targetId;
        this.imageUrl = imageUrl;
    }

    public void update(final Long targetId){
        this.targetId = targetId;
    }
}
