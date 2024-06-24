package nbdream.farm.repository;

import nbdream.farm.domain.FarmingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmingDiaryRepository extends JpaRepository<FarmingDiary, Long> {
}
