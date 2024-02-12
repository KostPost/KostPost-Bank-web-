package org.web.webauthorization.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.account.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByAccountName(String accountName);

    Account findByAccountPassword(String accountPassword);

}