package nbdream.farm.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.FarmingDiary;
import nbdream.farm.service.dto.farmingdiary.SearchFarmingDiaryCond;
import org.springframework.stereotype.Repository;

import java.util.List;

import static nbdream.farm.domain.QFarmingDiary.farmingDiary;
import static nbdream.farm.domain.QWork.work;

@Repository
@RequiredArgsConstructor
public class SearchFarmingDiaryRepository {
    private final JPAQueryFactory queryFactory;

    public List<FarmingDiary> getFarmingDiary(Long farmId, SearchFarmingDiaryCond cond) {
        JPQLQuery<Long> queryInWorkContents = JPAExpressions
                .select(work.farmingDiary.id)
                .from(work)
                .where(
                        work.farmingDiary.farm.id.eq(farmId),
                        work.content.contains(cond.getQuery())
                );

        return queryFactory.selectFrom(farmingDiary)
                .where(
                        farmingDiary.farm.id.eq(farmId),
                        farmingDiary.crop.eq(cond.getCrop()),
                        memoOrWorkCondition(cond.getQuery(), queryInWorkContents), // farmingDiary의 메모에 query 값이 포함되어 있거나 작업 내용 중에 query가 포함되어 있으면
                        farmingDiary.date.between(cond.getStartDate(), cond.getEndDate())
                )
                .fetch();
    }

    public BooleanExpression memoOrWorkCondition(String query, JPQLQuery<Long> queryInWorkContents) {
        return farmingDiary.memo.contains(query).or(farmingDiary.id.in(queryInWorkContents));
    }

}
