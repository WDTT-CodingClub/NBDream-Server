package nbdream.accountBook.controller;

import lombok.RequiredArgsConstructor;
import nbdream.accountBook.domain.AccountBook;
import nbdream.accountBook.service.AccountBookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;

}