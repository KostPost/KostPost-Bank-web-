package org.web.webauthorization.BankDataRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.Accounts;
import org.web.webauthorization.BankData.Deposit;

import java.util.ArrayList;

@Repository
public interface DepositRepository  extends JpaRepository<Deposit, Long> {

    ArrayList<Deposit> findByOwnerID(Long id);


}
