package nbdream.ledger.repository;

import nbdream.ledger.domain.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
}
