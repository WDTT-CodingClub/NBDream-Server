package nbdream.farm.repository;

import nbdream.farm.domain.FarmCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmCropRepository extends JpaRepository<FarmCrop, Long> {
}
