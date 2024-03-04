package org.web.webauthorization.BankData;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "deposit")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deposit_id")
    private Long depositID;

    @Column(name = "owner_id")
    private Long ownerID;

    @Column(name = "deposit_name")
    private String depositName;

    @Column(name = "deposit_sum")
    private BigDecimal depositSum;

}
