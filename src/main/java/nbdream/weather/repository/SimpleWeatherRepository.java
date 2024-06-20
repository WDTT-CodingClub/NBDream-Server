package nbdream.weather.repository;

import nbdream.weather.domain.SimpleWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SimpleWeatherRepository extends JpaRepository<SimpleWeather, Long> {
    @Modifying
    @Query("DELETE FROM SimpleWeather s WHERE s.farm.id = :farmId")
    void deleteAllByFarmId(@Param("farmId") Long farmId);

    @Query("SELECT s FROM SimpleWeather s WHERE s.farm.id = :farmId " +
            "ORDER BY s.date ASC " +
            "LIMIT 6")
    List<SimpleWeather> findByWeeksSimpleWeather(@Param("farmId") Long farmId);
}
