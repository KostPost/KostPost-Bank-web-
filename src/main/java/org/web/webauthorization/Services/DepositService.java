package org.web.webauthorization.Services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.Deposit;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.DepositRepository;
import org.web.webauthorization.BankDataRepository.UserAccountRepository;

@Service
public class DepositService {


    private final DepositRepository depositRepository;

    private final UserAccountRepository userAccountRepository;
    private final TransactionService transactionService;

    @Autowired
    public DepositService(UserAccountRepository userAccountRepository, TransactionService transactionService,
                          DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
        this.userAccountRepository = userAccountRepository;
        this.transactionService = transactionService;
    }

    public void withdrawMoney(UserAccount ownerDeposit, Deposit deposit){



    }

}
