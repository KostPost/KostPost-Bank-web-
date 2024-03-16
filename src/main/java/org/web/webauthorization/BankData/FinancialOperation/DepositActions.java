package org.web.webauthorization.BankData.FinancialOperation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@DiscriminatorValue("deposit_history")
public class DepositActions extends FinancialOperation {

    private BigDecimal userBalanceBeforeOperation;
    private BigDecimal userBalanceAfterOperation;

    @Override
    public boolean canEqual(Object other) {
        return other instanceof DepositActions;
    }

}