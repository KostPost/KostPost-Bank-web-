package org.web.webauthorization.Services;

import java.math.BigDecimal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.webauthorization.BankData.Transaction;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.TransactionRepository;

@Service
@Data
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void createNewTransfer(UserAccount sender, UserAccount recipient, BigDecimal transferSum){

        Long senderID = sender.getId(), recipientID = recipient.getId();

        Transaction newTransaction = new Transaction();

        newTransaction.setTransferSum(transferSum);

        newTransaction.setSender(sender.getAccountName());
        newTransaction.setSenderID(sender.getId());
        newTransaction.setSenderBalanceBeforeTransaction(sender.getAccountBalance());

        newTransaction.setRecipient(recipient.getAccountName());
        newTransaction.setRecipientID(recipient.getId());
        newTransaction.setRecipientBalanceBeforeTransaction(recipient.getAccountBalance());

        newTransaction.setSenderBalanceAfterTransaction(sender.getAccountBalance().subtract(transferSum));
        newTransaction.setRecipientBalanceAfterTransaction(recipient.getAccountBalance().add(transferSum));

        transactionRepository.save(newTransaction);


    }



}
