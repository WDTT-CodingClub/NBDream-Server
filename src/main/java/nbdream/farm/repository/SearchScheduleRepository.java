package nbdream.farm.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.alarm.domain.Alarm;
import nbdream.alarm.domain.QAlarm;
import nbdream.alarm.dto.AlarmScheduleDto;
import nbdream.farm.domain.QFarm;
import nbdream.farm.domain.QSchedule;
import nbdream.farm.domain.Schedule;
import nbdream.member.domain.QMember;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static nbdream.farm.domain.QSchedule.schedule;


@Repository
@RequiredArgsConstructor
public class SearchScheduleRepository {
    private final JPAQueryFactory queryFactory;

    private final QSchedule schedule = QSchedule.schedule;
    private final QFarm farm = QFarm.farm;
    private final QMember member = QMember.member;
    private final QAlarm alarm = QAlarm.alarm;

    public List<Schedule> searchSchedule(Long farmId, String category, LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(schedule)
                .where(
                        schedule.farm.id.eq(farmId),
                        categoryCond(category),
                        dateCond(startDate, endDate)
                )
                .fetch();
    }

    private BooleanExpression dateCond(LocalDate startDate, LocalDate endDate) {
        return (schedule.startDate.loe(endDate).and(schedule.endDate.goe(startDate)));
    }

    private BooleanExpression categoryCond(String category) {
        return (category == null) ? null : schedule.category.eq(category);
    }

    public List<AlarmScheduleDto> findAlarmsForSchedules(LocalDateTime alarmDateTime) {
        LocalDateTime endDateTime = alarmDateTime.plusHours(1);

        return queryFactory.select(Projections.constructor(
                        AlarmScheduleDto.class,
                        alarm,
                        schedule
                ))
                .from(schedule)
                .join(schedule.farm, farm).fetchJoin()
                .join(farm.member, member).fetchJoin()
                .join(alarm).on(alarm.member.eq(member)).fetchJoin()
                .where(
                        schedule.isAlarmOn.isTrue(),
                        alarmTimeCond(alarmDateTime, endDateTime)
                )
                .fetch();
    }

    private BooleanExpression alarmTimeCond(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return schedule.alarmDateTime.between(startDateTime, endDateTime.minusMinutes(1));
    }
}
