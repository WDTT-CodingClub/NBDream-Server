package nbdream.farm.repository;

import nbdream.farm.domain.FarmCrop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmCropRepository extends JpaRepository<FarmCrop, Long> {

    @Query("SELECT f FROM FarmCrop f JOIN FETCH f.crop fc WHERE f.farm.id = :farmId")
    List<FarmCrop> findByFarmId(@Param("farmId") Long farmId);
}
