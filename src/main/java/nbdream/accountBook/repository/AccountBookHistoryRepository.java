package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookHistoryRepository extends JpaRepository<AccountBookHistory, Long> {
}
