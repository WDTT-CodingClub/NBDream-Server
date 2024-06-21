package nbdream.bulletin.repository;

import nbdream.bulletin.domain.Bulletin;
import nbdream.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

    @Query("SELECT b FROM Bulletin b LEFT JOIN FETCH b.comments WHERE b.id = :bulletinId")
    Optional<Bulletin> findByIdFetchComments(@Param("bulletinId") Long bulletinId);

    @Query("SELECT b FROM Bulletin b WHERE b.id IN :bulletinIds AND b.id < :lastBulletinId " +
            "ORDER BY b.id DESC " +
            "LIMIT 11")
    List<Bulletin> findByIdsWithPaging(@Param("bulletinIds") List<Long> bulletinIds, @Param("lastBulletinId") Long lastBulletinId);

    @Query("SELECT b FROM Bulletin b WHERE b.author.id = :authorId AND b.id < :lastBulletinId " +
            "ORDER BY b.id DESC " +
            "LIMIT 11")
    List<Bulletin> findByAuthorWithPaging(@Param("authorId") Long authorId, @Param("lastBulletinId") Long lastBulletinId);
}
