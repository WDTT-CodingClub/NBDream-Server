package nbdream.bulletin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.bulletin.domain.Bulletin;
import nbdream.bulletin.domain.BulletinCategory;
import nbdream.bulletin.dto.request.SearchBulletinCondDto;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<Bulletin> searchBulletins(SearchBulletinCondDto cond, int size) {
        return queryFactory.select(bulletin)
                .from(bulletin)
                .where(keywordLike(cond.getKeyword()),
                        categoryEq(BulletinCategory.of(cond.getBulletinCategory())),
                        cropEq(cond.getCrop()),
                        bulletin.id.lt(cond.getLastBulletinId()))
                .limit(size)
                .orderBy(bulletin.id.desc())
                .fetch();
    }
}
