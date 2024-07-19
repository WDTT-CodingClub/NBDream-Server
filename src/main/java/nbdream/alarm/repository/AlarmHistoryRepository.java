package nbdream.alarm.repository;

import nbdream.alarm.domain.AlarmHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmHistoryRepository extends JpaRepository<AlarmHistory, Long> {

    List<AlarmHistory> findByAlarmId(Long id);
}
