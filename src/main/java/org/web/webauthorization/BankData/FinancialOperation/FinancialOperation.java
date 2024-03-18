package org.web.webauthorization.BankData.FinancialOperation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "operation_type", discriminatorType = DiscriminatorType.STRING)
public abstract class FinancialOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operationId;

    @Column(name = "operation_date")
    private LocalDate operationDate = LocalDate.now();

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "operation_creator_id")
    private Long operationCreatorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_action")
    public DepositActions depositActions;
    @Getter
    public enum DepositActions {
        WITHDRAW("Withdraw"),
        DEPOSIT("Deposit"),
        DELETE("Delete");

        private final String description;
        DepositActions(String description) {
            this.description = description;
        }
    }

    @Transient
    public TransactionType transactionType;
    @Getter
    public enum TransactionType {
        SEND("Send"),
        RECEIVED("Received");
        private final String description;
        TransactionType(String description) {
            this.description = description;
        }

    }


}