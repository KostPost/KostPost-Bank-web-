package org.web.webauthorization.BankDataRepository.FinancialOperation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.FinancialOperation.Deposit;

import java.util.ArrayList;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    ArrayList<Deposit> findByOwnerID(Long id);


}
