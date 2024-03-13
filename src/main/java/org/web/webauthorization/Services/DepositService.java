package org.web.webauthorization.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.FinancialOperation.Deposit;
import org.web.webauthorization.BankData.Accounts.UserAccount;
import org.web.webauthorization.BankDataRepository.FinancialOperation.DepositRepository;
import org.web.webauthorization.BankDataRepository.Accounts.UserAccountRepository;

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
