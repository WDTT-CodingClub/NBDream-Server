package nbdream.farm.repository;

import nbdream.farm.domain.LandElements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandElementsRepository extends JpaRepository<LandElements, Long> {
}
