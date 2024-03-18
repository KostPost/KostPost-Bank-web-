package org.web.webauthorization.BankDataRepository.FinancialOperation;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.FinancialOperation.Deposit;

import java.util.ArrayList;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    ArrayList<Deposit> findByOwnerID(Long id);
    @Modifying
    @Transactional
    void deleteByDepositID(Long id);

}
