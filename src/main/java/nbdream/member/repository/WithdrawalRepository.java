package nbdream.member.repository;

import nbdream.member.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
