package nbdream.bulletin.repository;

import nbdream.bulletin.domain.Bookmark;
import nbdream.bulletin.domain.Bulletin;
import nbdream.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE b.member = :member AND b.bulletin = :bulletin")
    Optional<Bookmark> findByMemberAndBulletin(@Param("member") Member member, @Param("bulletin") Bulletin bulletin);

    @Query("SELECT b FROM Bookmark b WHERE b.member.id = :memberId AND b.bulletin.id = :bulletinId")
    Optional<Bookmark> findByMemberIdAndBulletinId(@Param("memberId") Long memberId, @Param("bulletinId") Long bulletinId);

    @Query("SELECT b FROM Bookmark b WHERE b.member = :member")
    List<Bookmark> findByMember(@Param("member") Member member);

    @Query("SELECT b FROM Bookmark b WHERE b.member.id = :memberId")
    List<Bookmark> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT b FROM Bookmark b WHERE b.bulletin.id = :bulletinId")
    List<Bookmark> findByBulletinId(@Param("bulletinId") Long bulletinId);

    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.bulletin.id = :bulletinId")
    void deleteAllByBulletinId(@Param("bulletinId") Long bulletinId);

    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}

