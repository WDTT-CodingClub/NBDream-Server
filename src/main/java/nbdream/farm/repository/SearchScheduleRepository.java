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
                        schedule.category.eq(category),
                        dateCond(startDate, endDate)
                )
                .fetch();
    }

    private BooleanExpression dateCond(LocalDate startDate, LocalDate endDate) {
        return (schedule.startDate.loe(endDate).and(schedule.endDate.goe(startDate)));
    }
}
