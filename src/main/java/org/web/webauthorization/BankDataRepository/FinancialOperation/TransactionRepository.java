package org.web.webauthorization.BankDataRepository.FinancialOperation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.FinancialOperation.FinancialOperation;
import org.web.webauthorization.BankData.FinancialOperation.Transaction;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    ArrayList<Transaction> findBySender(String FirstName);

    ArrayList<Transaction> findByRecipient(String FirstName);


}