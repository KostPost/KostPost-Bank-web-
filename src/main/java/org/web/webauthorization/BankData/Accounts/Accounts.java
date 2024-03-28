package org.web.webauthorization.BankData.Accounts;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.web.webauthorization.BankData.FinancialOperation.FinancialOperation;

import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Accounts {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_password")
    private String accountPassword;


    @Enumerated(EnumType.STRING)
    @Column(name = "account_role")
    public AccountRole accountRole;

    @Getter
    public enum AccountRole {
        ADMIN("Admin"),
        USER("User");

        private final String description;
        AccountRole(String description) {
            this.description = description;
        }
    }
}