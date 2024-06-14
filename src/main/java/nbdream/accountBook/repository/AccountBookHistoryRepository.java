package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface AccountBookHistoryRepository
        extends JpaRepository<AccountBookHistory, Long>,
        JpaSpecificationExecutor<AccountBookHistory>,
        PagingAndSortingRepository<AccountBookHistory, Long>,
        AccountBookHistoryRepositoryCustom {

    List<AccountBookHistory> findByMemberIdAndCursor(Long memberId, Long cursor, int maxResults, GetAccountBookListReqDto request);
}
