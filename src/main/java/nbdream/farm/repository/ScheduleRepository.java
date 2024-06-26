package nbdream.farm.repository;

import nbdream.farm.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByFarmId(Long id);
}
