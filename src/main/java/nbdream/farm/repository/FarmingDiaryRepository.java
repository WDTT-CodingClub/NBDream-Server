package nbdream.farm.repository;

import nbdream.farm.domain.FarmingDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmingDiaryRepository extends JpaRepository<FarmingDiary, Long> {
}
