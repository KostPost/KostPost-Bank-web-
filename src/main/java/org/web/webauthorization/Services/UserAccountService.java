package org.web.webauthorization.Services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.UserAccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    public void updateUserAccountBalance(Long senderId, Long recipientId, BigDecimal transferSum) {

        Optional<UserAccount> userSenderOpt = userAccountRepository.findById(senderId);
        Optional<UserAccount> userRecipientOpt = userAccountRepository.findById(recipientId);

        UserAccount userSender = userSenderOpt.get();
        UserAccount userRecipient = userRecipientOpt.get();


        userSender.setAccountBalance(userSender.getAccountBalance().subtract(transferSum));

        userRecipient.setAccountBalance(userRecipient.getAccountBalance().add(transferSum));

        userAccountRepository.save(userSender);
        userAccountRepository.save(userRecipient);
    }

}
