package nbdream.weather.repository;

import nbdream.weather.domain.code.TemperatureRegionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TemperatureRegionCodeRepository extends JpaRepository<TemperatureRegionCode, Long> {
    @Query("SELECT t FROM TemperatureRegionCode t WHERE :address LIKE CONCAT('%', t.regionName, '%')")
    Optional<TemperatureRegionCode> findByAddress(@Param("address") String address);
}
