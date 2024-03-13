package org.web.webauthorization.Services;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.FinancialOperation.Transaction;
import org.web.webauthorization.BankData.Accounts.UserAccount;
import org.web.webauthorization.BankDataRepository.FinancialOperation.TransactionRepository;

@Service
@Data
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void createNewTransfer(UserAccount sender, UserAccount recipient, BigDecimal transferSum, String comment){

        Transaction newTransaction = new Transaction();

        newTransaction.setSenderId(sender.getId());

        newTransaction.setComment(comment);

        newTransaction.setAmount(transferSum);

        newTransaction.setSender(sender.getAccountName());
        newTransaction.setSenderId(sender.getId());
        newTransaction.setSenderBalanceBeforeTransaction(sender.getAccountBalance());

        newTransaction.setRecipient(recipient.getAccountName());
        newTransaction.setRecipientId(recipient.getId());
        newTransaction.setRecipientBalanceBeforeTransaction(recipient.getAccountBalance());

        newTransaction.setSenderBalanceAfterTransaction(sender.getAccountBalance().subtract(transferSum));
        newTransaction.setRecipientBalanceAfterTransaction(recipient.getAccountBalance().add(transferSum));

        System.out.println("\n\n\n add tran \n\n\n");

        transactionRepository.save(newTransaction);


    }



}
