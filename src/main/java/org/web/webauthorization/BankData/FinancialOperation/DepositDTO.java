package org.web.webauthorization.BankData.FinancialOperation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositDTO {

    private String name;
    private BigDecimal currentAmount;
    private BigDecimal goalAmount;

    public DepositDTO(String name, BigDecimal currentAmount, BigDecimal goalAmount) {
        this.name = name;
        this.currentAmount = currentAmount;
        this.goalAmount = goalAmount;
    }

}
