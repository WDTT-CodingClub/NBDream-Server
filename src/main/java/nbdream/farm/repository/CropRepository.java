package nbdream.farm.repository;

import nbdream.farm.domain.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByName(String name);
}
