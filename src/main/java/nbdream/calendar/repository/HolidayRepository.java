package nbdream.calendar.repository;

import nbdream.calendar.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findAllByFarmingDiaryId(Long farmingDiaryId);
}
