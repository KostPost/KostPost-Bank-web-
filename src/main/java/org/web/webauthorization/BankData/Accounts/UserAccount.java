package org.web.webauthorization.BankData.Accounts;

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

    public static String removeSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }


}
