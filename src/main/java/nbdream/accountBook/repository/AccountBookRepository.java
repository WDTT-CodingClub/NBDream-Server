package nbdream.accountBook.repository;

import nbdream.accountBook.domain.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
}
