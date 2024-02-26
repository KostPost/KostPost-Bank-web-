package org.web.webauthorization.BankData;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@DiscriminatorValue("user")
public class UserAccount extends Accounts {

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    @Column(name = "card_number")
    private String cardNumber;

    public UserAccount(){
        this.accountBalance = BigDecimal.valueOf(500);
    }

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int digit = ThreadLocalRandom.current().nextInt(0, 10);
            cardNumber.append(digit);
        }

        return cardNumber.toString();
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.accountBalance = this.accountBalance.add(amount);
        } else {
            throw new IllegalArgumentException("Сумма для пополнения должна быть больше нуля");
        }
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && this.accountBalance.compareTo(amount) >= 0) {
            this.accountBalance = this.accountBalance.subtract(amount);
        } else {
            throw new IllegalArgumentException("Недостаточно средств на счете или сумма для снятия меньше нуля");
        }
    }

}
