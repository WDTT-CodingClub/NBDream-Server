package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountBookHistoryRepositoryCustom {
    List<AccountBookHistory> findByMemberIdAndCursor(Long memberId, Long cursor, int pageSize, GetAccountBookListReqDto reqDto);
}
