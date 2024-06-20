package nbdream.weather.repository;

import nbdream.weather.domain.code.SkyRegionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SkyRegionCodeRepository extends JpaRepository<SkyRegionCode, Long> {

    @Query("SELECT s FROM SkyRegionCode s WHERE :address LIKE CONCAT('%', s.regionName, '%')")
    Optional<SkyRegionCode> findByAddress(@Param("address") String address);
}
