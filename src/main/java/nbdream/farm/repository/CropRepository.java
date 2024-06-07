package nbdream.farm.repository;

import nbdream.farm.domain.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}
