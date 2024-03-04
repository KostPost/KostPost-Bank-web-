package org.web.webauthorization.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.web.webauthorization.BankData.Deposit;
import org.web.webauthorization.BankData.Transaction;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.DepositRepository;
import org.web.webauthorization.BankDataRepository.TransactionRepository;
import org.web.webauthorization.BankDataRepository.UserAccountRepository;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.ui.Model;
import org.web.webauthorization.Services.UserAccountService;

@Controller
public class MainController {

    private final UserAccountRepository userAccountRepository;

    private final DepositRepository depositRepository;

    private final UserAccountService userAccountService;

    private final TransactionRepository transactionRepository;

    UserAccount mainUser = null;

    @Autowired
    public MainController(UserAccountRepository userAccountRepository, TransactionRepository transactionRepository,
                          UserAccountService userAccountService, DepositRepository depositRepository) {
        this.userAccountRepository = userAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userAccountService = userAccountService;
        this.depositRepository = depositRepository;
    }

    @GetMapping("/transaction-details/{transactionId}")
    @ResponseBody
    public Transaction getTransactionDetails(@PathVariable Long transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @GetMapping("/main")
    public String mainPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            mainUser = userAccountRepository.findByAccountName(username);

            UserAccount userAccount = userAccountRepository.findByAccountName(username);
            BigDecimal balance = userAccount.getAccountBalance();

            model.addAttribute("userBalance", balance);
        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {

            ArrayList<Deposit> deposits = depositRepository.findByOwnerID(mainUser.getId());
            model.addAttribute("deposits", deposits);

        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            List<Transaction> transactions = transactionRepository.findBySenderNameOrRecipientName(username);
            Collections.reverse(transactions);
            transactions.forEach(transaction -> {
                if (transaction.getSender().equals(username)) {
                    transaction.setTransactionType("Sent");

                    //System.out.println("\n\n\n" +transaction.getTransactionType() + "\n\n\n");
                } else if (transaction.getRecipient().equals(username)) {
                    transaction.setTransactionType("Received");
                    //System.out.println("\n\n\n" +transaction.getTransactionType() + "\n\n\n");

                }
            });
            model.addAttribute("transactions", transactions);

        }
        return "main";
    }


    public static String removeSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }

    @PostMapping("/transfer")
    @ResponseBody
    public ResponseEntity<Map<String, String>> transfer(@RequestParam("card-number") String cardNumber,
                                                        @RequestParam("amount") BigDecimal transferSum,
                                                        @RequestParam("comment") String comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        String realCardNumber = removeSpaces(cardNumber);
        UserAccount userSender = userAccountRepository.findByAccountName(currentPrincipalName);
        UserAccount userRecipient = userAccountRepository.findByCardNumber(realCardNumber);

        Map<String, String> response = new HashMap<>();
        String error = null;

        if (userRecipient == null) {
            error = "Recipient not found";
        } else if (userSender.getId().equals(userRecipient.getId())) {
            error = "Cannot transfer to the same account";
        } else if (transferSum.compareTo(BigDecimal.ZERO) <= 0) {
            error = "The amount must be greater than 0";
        } else if (transferSum.compareTo(userSender.getAccountBalance()) > 0) {
            error = "Insufficient funds";
        }

        if (error != null) {
            response.put("success", "false");
            response.put("message", error);
            return ResponseEntity.badRequest().body(response);
        }

        userAccountService.newTransfer(userSender.getId(), userRecipient.getId(), transferSum);

        response.put("success", "true");
        response.put("message", "Transaction successful");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/createDeposit")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createDeposit(@RequestParam("deposit-name") String depositName,
                                                             @RequestParam("deposit-amount") BigDecimal depositSum) {

        Map<String, String> response = new HashMap<>();
        String error = null;

        if (depositSum.compareTo(BigDecimal.ZERO) < 0) {
            error = "deposit sum can't be less than 0";
        }


        Deposit deposit = new Deposit();
        deposit.setDepositName(depositName);
        deposit.setDepositSum(depositSum);
        deposit.setOwnerID(mainUser.getId());

        if (error != null) {
            response.put("success", "false");
            response.put("message", error);
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println(deposit.getDepositID() + "\n" + deposit.getOwnerID() + "\n" + deposit.getDepositSum() + "\n" + deposit.getDepositName());

        depositRepository.save(deposit);

        response.put("success", "true");
        response.put("message", "Transaction successful");
        return ResponseEntity.ok().body(response);
    }

}