package nbdream.farm.repository;

import nbdream.farm.domain.FarmingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmingDiaryRepository extends JpaRepository<FarmingDiary, Long> {

    @Query("SELECT f FROM FarmingDiary f WHERE f.farm.id = :farmId AND " +
            "MONTH(f.date) = :month AND YEAR(f.date) = :year AND f.crop = :crop " +
            "ORDER BY f.date DESC")
    List<FarmingDiary> findAllByFarmIdWithCond(@Param("farmId") Long farmId, @Param("month") int month,
                                               @Param("year") int year, @Param("crop") String crop);

    @Query("SELECT f FROM FarmingDiary f WHERE f.farm.id = :farmId")
    List<FarmingDiary> findAllByFarmId(@Param("farmId") Long farmId);
}
