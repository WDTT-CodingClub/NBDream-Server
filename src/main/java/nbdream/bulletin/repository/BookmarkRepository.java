package nbdream.bulletin.repository;

import nbdream.bulletin.domain.Bookmark;
import nbdream.bulletin.domain.Bulletin;
import nbdream.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE b.member = :member AND b.bulletin = :bulletin")
    Optional<Bookmark> findByMemberAndBookmark(@Param("member") Member member, @Param("bulletin") Bulletin bulletin);

    @Query("SELECT b FROM Bookmark b WHERE b.member = :member")
    List<Bookmark> findByMember(@Param("member") Member member);
}
