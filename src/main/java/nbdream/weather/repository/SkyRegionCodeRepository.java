package nbdream.weather.repository;

import nbdream.weather.domain.code.SkyRegionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SkyRegionCodeRepository extends JpaRepository<SkyRegionCode, Long> {

    @Query(value = "SELECT * FROM sky_region_code WHERE :address LIKE CONCAT('%', region_name, '%') " +
            "LIMIT 1", nativeQuery = true)
    Optional<SkyRegionCode> findByAddress(@Param("address") String address);
}
