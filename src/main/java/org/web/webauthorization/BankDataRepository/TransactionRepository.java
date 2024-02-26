package org.web.webauthorization.BankDataRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.Transaction;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    ArrayList<Transaction> findBySender(String FirstName);

    ArrayList<Transaction> findByRecipient(String FirstName);

    @Query("SELECT t FROM Transaction t WHERE t.sender = :username OR t.recipient = :username")
    List<Transaction> findBySenderNameOrRecipientName(String username);
}