package org.web.webauthorization.Services;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.Accounts.UserAccount;
import org.web.webauthorization.BankData.FinancialOperation.Deposit;
import org.web.webauthorization.BankData.FinancialOperation.DepositActions;
import org.web.webauthorization.BankData.FinancialOperation.FinancialOperation;
import org.web.webauthorization.BankDataRepository.FinancialOperation.DepositActionsRepository;
import org.web.webauthorization.BankDataRepository.FinancialOperation.DepositRepository;
import org.web.webauthorization.BankDataRepository.Accounts.UserAccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Data
public class DepositService {

    private final DepositRepository depositRepository;

    private final UserAccountRepository userAccountRepository;
    private final DepositActionsRepository depositActionsRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository,
                          UserAccountRepository userAccountRepository,
                          DepositActionsRepository depositActionsRepository) {
        this.depositRepository = depositRepository;
        this.userAccountRepository = userAccountRepository;
        this.depositActionsRepository = depositActionsRepository;
    }

    public void handleDepositActionMoney(Long depositId, BigDecimal depositAmount, UserAccount user, String actionType) {
        Optional<Deposit> depositOptional = depositRepository.findById(depositId);

        if (depositOptional.isPresent()) {
            Deposit deposit = depositOptional.get();
            DepositActions depositActions = new DepositActions();

            depositActions.setOperationCreatorId(user.getId());
            depositActions.setUserBalanceBeforeOperation(user.getAccountBalance());
            depositActions.setAmount(depositAmount);
            if ("WITHDRAW".equals(actionType)) {
                // Проверка на достаточность средств для снятия
                if (deposit.getDepositCurrentAmount().compareTo(depositAmount) >= 0) {
                    deposit.setDepositCurrentAmount(deposit.getDepositCurrentAmount().subtract(depositAmount));
                    user.setAccountBalance(user.getAccountBalance().add(depositAmount));
                    depositActions.setDepositActions(FinancialOperation.DepositActions.WITHDRAW);
                }
            } else if ("DEPOSIT".equals(actionType)) {
                // Проверка на достаточность средств на счету пользователя
                if (user.getAccountBalance().compareTo(depositAmount) >= 0) {
                    deposit.setDepositCurrentAmount(deposit.getDepositCurrentAmount().add(depositAmount));
                    user.setAccountBalance(user.getAccountBalance().subtract(depositAmount));
                    depositActions.setDepositActions(FinancialOperation.DepositActions.DEPOSIT);
                }
            }

            depositActions.setUserBalanceAfterOperation(user.getAccountBalance());

            depositActionsRepository.save(depositActions);
            depositRepository.save(deposit);
            userAccountRepository.save(user);
        }
    }

    @Transactional
    public void deleteDeposit(Long depositId, UserAccount user){

        Optional<Deposit> depositOptional = depositRepository.findById(depositId);
        DepositActions depositActions = new DepositActions();

        if (depositOptional.isPresent()) {
            Deposit deposit = depositOptional.get();


            depositActions.setOperationCreatorId(user.getId());
            depositActions.setUserBalanceBeforeOperation(user.getAccountBalance());
            depositActions.setAmount(deposit.getDepositCurrentAmount());
            depositActions.setDepositActions(FinancialOperation.DepositActions.DELETE);
            System.out.println(deposit.getDepositName());


            user.setAccountBalance(user.getAccountBalance().add(deposit.getDepositCurrentAmount()));


            depositActions.setUserBalanceAfterOperation(user.getAccountBalance());
        }


        depositRepository.deleteByDepositID(depositId);
        userAccountRepository.save(user);
        depositActionsRepository.save(depositActions);


    }


}
