package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {

    Optional<AccountBook> findByMemberId(Long memberId);
}
