package org.web.webauthorization.BankData;



import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "operation_id")
    private Long transactionID;

    @Column(name = "transaction_sum")
    private BigDecimal transferSum;


    @Column(name = "sender")
    private String sender;
    @Column(name = "sender_id")
    private Long senderID;
    @Column(name = "sender_balance_before_operation")
    private BigDecimal senderBalanceBeforeTransaction;
    @Column(name = "sender_balance_after_operation")
    private BigDecimal senderBalanceAfterTransaction;

    @Column(name = "recipient")
    private String recipient;
    @Column(name = "recipient_id")
    private Long recipientID;
    @Column(name = "recipient_balance_before_operation")
    private BigDecimal recipientBalanceBeforeTransaction;
    @Column(name = "recipient_balance_after_operation")
    private BigDecimal recipientBalanceAfterTransaction;

    @Column(name = "account_creation_date")
    private LocalDate transactionDate = LocalDate.now();

}