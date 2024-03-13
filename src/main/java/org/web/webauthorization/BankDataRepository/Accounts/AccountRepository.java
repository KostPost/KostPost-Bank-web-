package org.web.webauthorization.BankDataRepository.Accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.Accounts.Accounts;
import org.web.webauthorization.BankData.Accounts.UserAccount;

@Repository
public interface AccountRepository extends CrudRepository<Accounts, Long> {

    UserAccount findByAccountName(String accountName);

    UserAccount findByCardNumber(String cardNumber);

}