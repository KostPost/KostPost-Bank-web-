package org.web.webauthorization.BankDataRepository.Accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.Accounts.UserAccount;

import java.util.UUID;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByCardNumber(String cardNumber);

    UserAccount findByAccountName(String accountName);


}
