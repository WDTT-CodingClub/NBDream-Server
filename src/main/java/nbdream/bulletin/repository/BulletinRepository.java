package nbdream.bulletin.repository;

import nbdream.bulletin.domain.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

    @Query("SELECT b FROM Bulletin b JOIN FETCH b.comments WHERE b.id = :bulletinId")
    Optional<Bulletin> findByIdFetchComments(@Param("bulletinId") Long bulletinId);
}
