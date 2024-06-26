package nbdream.farm.repository;

import nbdream.farm.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findAllByFarmingDiaryId(Long farmingDiaryId);

}
