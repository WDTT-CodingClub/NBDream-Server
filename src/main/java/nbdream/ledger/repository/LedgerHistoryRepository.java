package nbdream.ledger.repository;

import nbdream.ledger.domain.LedgerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerHistoryRepository extends JpaRepository<LedgerHistory, Long> {
}
