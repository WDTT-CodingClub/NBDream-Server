package nbdream.farm.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.Schedule;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static nbdream.farm.domain.QSchedule.schedule;


@Repository
@RequiredArgsConstructor
public class SearchScheduleRepository {
    private final JPAQueryFactory queryFactory;

    public List<Schedule> searchSchedule(Long farmId, String category, LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(schedule)
                .where(
                        schedule.farm.id.eq(farmId),
                        categoryEq(category),
                        dateCond(startDate, endDate)
                )
                .fetch();
    }

    //전체면 전체검색, 작물이름이면 작물이름+전체
    private BooleanExpression categoryEq(String category) {
        return category.equals("전체") ? null : schedule.category.eq(category).or(schedule.category.eq("전체"));
    }

    private BooleanExpression dateCond(LocalDate startDate, LocalDate endDate) {
        return (schedule.startDate.loe(endDate).and(schedule.endDate.goe(startDate)));
    }
}
