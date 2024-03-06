package org.web.webauthorization.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.Transaction;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.UserAccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final TransactionService transactionService;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository, TransactionService transactionService) {
        this.userAccountRepository = userAccountRepository;
        this.transactionService = transactionService;
    }


    public void newTransfer(Long senderId, Long recipientId, BigDecimal transferSum, String comment) {

        Optional<UserAccount> userSenderOpt = userAccountRepository.findById(senderId);
        Optional<UserAccount> userRecipientOpt = userAccountRepository.findById(recipientId);

        UserAccount userSender = userSenderOpt.get();
        UserAccount userRecipient = userRecipientOpt.get();

        transactionService.createNewTransfer(userSender, userRecipient, transferSum, comment);


        userSender.setAccountBalance(userSender.getAccountBalance().subtract(transferSum));
        userRecipient.setAccountBalance(userRecipient.getAccountBalance().add(transferSum));


        userAccountRepository.save(userSender);
        userAccountRepository.save(userRecipient);
    }

}
