package org.web.webauthorization.BankData.Accounts;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("admin")
public class AdminAccount extends Accounts {

    @Column(name = "admin_level")
    private long level;



}