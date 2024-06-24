package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBookHistory;
import nbdream.accountBook.service.dto.GetAccountBookListReqDto;

import java.util.List;


public interface AccountBookHistoryRepositoryCustom {
    List<AccountBookHistory> findByFilterAndCursor(Long memberId, Long cursor, int pageSize, GetAccountBookListReqDto reqDto);
    List<AccountBookHistory> findAllByFilter(Long memberId, GetAccountBookListReqDto reqDto);
}
