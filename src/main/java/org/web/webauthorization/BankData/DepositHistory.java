package org.web.webauthorization.BankData;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("depositHistory")
public class DepositHistory extends FinancialOperation {



    private Long operationID;



}