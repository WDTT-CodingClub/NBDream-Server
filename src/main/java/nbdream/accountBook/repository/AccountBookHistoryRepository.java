package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface AccountBookHistoryRepository
        extends JpaRepository<AccountBookHistory, Long>,
        JpaSpecificationExecutor<AccountBookHistory>,
        PagingAndSortingRepository<AccountBookHistory, Long> {

}
