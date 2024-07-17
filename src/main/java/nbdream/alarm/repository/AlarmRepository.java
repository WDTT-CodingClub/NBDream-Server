package nbdream.alarm.repository;

import nbdream.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Alarm findByMemberId(Long memberId);
}
