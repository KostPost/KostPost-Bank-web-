package org.web.webauthorization.BankData.FinancialOperation;

import jakarta.persistence.*;
import lombok.Data;
import org.web.webauthorization.BankData.FinancialOperation.FinancialOperation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@DiscriminatorValue("deposit_history")
public class DepositHistory extends FinancialOperation {


    private BigDecimal userBalanceBeforeOperation;
    private BigDecimal userBalanceAfterOperation;

}