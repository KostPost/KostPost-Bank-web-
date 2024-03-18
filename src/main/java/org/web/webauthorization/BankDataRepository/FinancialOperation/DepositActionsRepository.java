package org.web.webauthorization.BankDataRepository.FinancialOperation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.FinancialOperation.DepositActions;
@Repository
public interface DepositActionsRepository extends JpaRepository<DepositActions, Long> {


}
