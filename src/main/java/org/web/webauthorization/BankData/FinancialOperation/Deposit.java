package org.web.webauthorization.BankData.FinancialOperation;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deposit_id")
    private Long depositID;

    @Column(name = "owner_id")
    private Long ownerID;

    @Column(name = "deposit_name")
    private String depositName;

    @Column(name = "deposit_current_sum")
    private BigDecimal depositCurrentAmount =  BigDecimal.valueOf(0);

    @Column(name = "deposit_goal_sum")
    private BigDecimal depositGoalAmount;

}
