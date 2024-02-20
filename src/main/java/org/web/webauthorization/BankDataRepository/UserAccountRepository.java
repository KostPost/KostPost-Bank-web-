package org.web.webauthorization.BankDataRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.UserAccount;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByCardNumber(String cardNumber);

    UserAccount findByAccountName(String accountName);


}
