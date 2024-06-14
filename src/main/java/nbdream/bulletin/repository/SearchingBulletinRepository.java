package nbdream.bulletin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.BulletinCategory;
import org.springframework.stereotype.Repository;

import static nbdream.bulletin.domain.QBulletin.bulletin;

@Repository
@RequiredArgsConstructor
public class SearchingBulletinRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression keywordLike(String keyword) {
        return (keyword == null || keyword.isEmpty()) ? null : bulletin.content.like("%" + keyword + "%");
    }

    private BooleanExpression categoryEq(BulletinCategory category) {
        return (category == null) ? null : bulletin.bulletinCategory.eq(category);
    }

    private BooleanExpression cropEq(String crop) {
        return (crop == null || crop.isEmpty()) ? null : bulletin.crop.eq(crop);
    }

}
