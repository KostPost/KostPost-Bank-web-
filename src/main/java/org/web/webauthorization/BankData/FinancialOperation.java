package org.web.webauthorization.BankData;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "operation_type", discriminatorType = DiscriminatorType.STRING)
public abstract class FinancialOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operation_id;

    @Column(name = "operation_date")
    private LocalDate operationDate = LocalDate.now();

    @Column(name = "amount")
    private BigDecimal amount; // Предположим, что обе операции включают сумму

}