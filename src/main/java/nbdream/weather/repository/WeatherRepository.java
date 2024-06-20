package nbdream.weather.repository;

import nbdream.weather.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w WHERE w.farm.id = :farmId AND w.date = :date")
    Optional<Weather> findByFarmIdAndDate(@Param("farmId") Long farmId, @Param("date") LocalDate date);
}
