package nbdream.farm.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbdream.farm.domain.FarmWorkSchedule;
import org.springframework.stereotype.Repository;

import java.util.List;

import static nbdream.farm.domain.QFarmWorkSchedule.farmWorkSchedule;


@Repository
@RequiredArgsConstructor
public class FarmWorkScheduleCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<String> findAllCropNames() {
        return queryFactory.select(farmWorkSchedule.workName)
                .from(farmWorkSchedule)
                .distinct()
                .fetch();
    }

    // 작물이름이 같은지 확인
    //      endMonth > 12 == true
    //          (입력 월 <= endMonth -12) or (beginMonth <= 입력 월) 인 컬럼 조회
    //      endMonth > 12 == false
    //          startMonth <= 입력 월 <= endMonth
    public List<FarmWorkSchedule> findSchedulesByCropAndMonth(String cropName, int month) {
        return queryFactory.selectFrom(farmWorkSchedule)
                .where(
                        cropNameEq(cropName),
                        monthCondition(month)
                )
                .fetch();
    }

    private BooleanExpression cropNameEq(String cropName) {
        return cropName != null ? farmWorkSchedule.workName.eq(cropName) : null;
    }

    private BooleanExpression monthCondition(int month) {
        return endMonthGt12().and(monthLteEndMonthMinus12(month).or(beginMonthLteMonth(month)))
                .or(endMonthLte12().and(monthBetween(month)));
    }

    private BooleanExpression endMonthGt12() {
        return farmWorkSchedule.endMonth.gt(12);
    }

    private BooleanExpression endMonthLte12() {
        return farmWorkSchedule.endMonth.loe(12);
    }

    private BooleanExpression monthLteEndMonthMinus12(int month) {
        return farmWorkSchedule.endMonth.subtract(12).goe(month);
    }

    private BooleanExpression beginMonthLteMonth(int month) {
        return farmWorkSchedule.beginMonth.loe(month);
    }

    private BooleanExpression monthBetween(int month) {
        return farmWorkSchedule.beginMonth.loe(month)
                .and(farmWorkSchedule.endMonth.goe(month));
    }

}
