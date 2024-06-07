package nbdream.bulletin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbdream.common.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bulletin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String crop;

    @Enumerated(EnumType.STRING)
    private BulletinCategory bulletinCategory;

    private String content;

    public Bulletin(final String crop, final BulletinCategory bulletinCategory, final String content) {
        this.crop = crop;
        this.bulletinCategory = bulletinCategory;
        this.content = content;
    }
}
