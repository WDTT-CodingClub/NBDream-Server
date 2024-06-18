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

    @Query("SELECT b FROM Bulletin b LEFT JOIN FETCH b.comments WHERE b.id IN :bulletinIds")
    List<Bulletin> findByIdsFetchComments(@Param("bulletinIds") List<Long> bulletinIds);

    @Query("SELECT b FROM Bulletin b LEFT JOIN FETCH b.comments WHERE b.author.id = :authorId")
    List<Bulletin> findByAuthorFetchComments(@Param("authorId") Long authorId);
}
