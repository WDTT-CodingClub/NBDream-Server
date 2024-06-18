package nbdream.farm.repository;

import nbdream.farm.domain.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    Optional<Farm> findByMemberId(Long id);
}
