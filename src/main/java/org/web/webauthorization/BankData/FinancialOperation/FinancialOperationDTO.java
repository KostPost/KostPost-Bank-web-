package org.web.webauthorization.BankData.FinancialOperation;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialOperationDTO {

    private Long operationId;
    private LocalDate operationDate;
    private BigDecimal amount;
    private Long senderId;
    private Long recipientId;
    private Long operationCreatorId;
    private String operationType;

    // DepositHistory specific fields
    private BigDecimal userBalanceBeforeOperation;
    private BigDecimal userBalanceAfterOperation;

    // Transaction specific fields
    private String sender;
    private BigDecimal senderBalanceBeforeTransaction;
    private BigDecimal senderBalanceAfterTransaction;
    private String recipient;
    private BigDecimal recipientBalanceBeforeTransaction;
    private BigDecimal recipientBalanceAfterTransaction;
    private String comment;
    

}
