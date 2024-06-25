package nbdream.farm.repository;

import nbdream.farm.domain.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FarmRepository extends JpaRepository<Farm, Long> {

    @Query("SELECT f FROM Farm f WHERE f.member.id = :memberId")
    Optional<Farm> findByMemberId(@Param("memberId") Long memberId);
}
