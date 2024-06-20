package nbdream.farm.repository;

import nbdream.farm.domain.FarmWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmWorkScheduleRepository extends JpaRepository<FarmWorkSchedule, Long> {
}
