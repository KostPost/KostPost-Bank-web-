package org.web.webauthorization.BankDataRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.UserAccount;

@Repository
public interface AccountRepository extends CrudRepository<UserAccount, Long> {

    UserAccount findByAccountName(String accountName);

    UserAccount findByCardNumber(String cardNumber);

}