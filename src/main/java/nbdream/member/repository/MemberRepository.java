package nbdream.member.repository;

import nbdream.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialId(String socialId);

    @Query("SELECT m FROM Member m JOIN FETCH m.farm WHERE m.id = :memberId")
    Optional<Member> findByIdFetchFarm(@Param("memberId") Long memberId);

    Optional<Member> findByNickname(String nickname);

}
