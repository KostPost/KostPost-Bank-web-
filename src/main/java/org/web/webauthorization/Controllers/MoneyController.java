package org.web.webauthorization.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.webauthorization.BankData.Transaction;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.UserAccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.ui.Model;
import org.web.webauthorization.Services.TransactionService;
import org.web.webauthorization.Services.UserAccountService;

@Controller
public class MoneyController {

    private final UserAccountRepository userAccountRepository;

    private final UserAccountService userAccountService;

    private final TransactionService transactionService;

    @Autowired
    public MoneyController(UserAccountRepository userAccountRepository, TransactionService transactionService,
                           UserAccountService userAccountService) {
        this.userAccountRepository = userAccountRepository;
        this.transactionService = transactionService;
        this.userAccountService = userAccountService;
    }



    @GetMapping("/main")
    public String mainPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            UserAccount userAccount = userAccountRepository.findByAccountName(username);
            BigDecimal balance = userAccount.getAccountBalance();

            model.addAttribute("userBalance", balance);
        }

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();

            // Предполагаем, что у вас есть метод в сервисе для получения списка транзакций по имени пользователя
            List<Transaction> transactions = transactionService.findTransactionsByUsername(username);

            model.addAttribute("transactions", transactions);
        }
        return "main";
    }


    public static String removeSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }


    @PostMapping("/transfer")
    public String transfer(@RequestParam("card-number") String cardNumber,
                           @RequestParam("amount") BigDecimal TransferSum,
                           @RequestParam("comment") String comment,
                           RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        String realCardNumber = removeSpaces(cardNumber);
        UserAccount userSender = userAccountRepository.findByAccountName(currentPrincipalName);
        UserAccount userRecipient = userAccountRepository.findByCardNumber(realCardNumber);


        String errorMessage = null;
        if (userRecipient == null) {
            errorMessage = "No recipient found with the provided card number.";
        }
        else if(Objects.equals(userSender.getCardNumber(), cardNumber)){
            errorMessage = "You can't send money to yourself";
        }
        else if (cardNumber.length() != 19) {
            errorMessage = "Card number must be exactly 19 digits long.";
        } else if (TransferSum.compareTo(BigDecimal.ZERO) <= 0) {
            errorMessage = "Transfer amount must be positive and greater than zero.";
        } else if (TransferSum.compareTo(userSender.getAccountBalance()) > 0) {
            errorMessage = "Insufficient funds for this transfer.";
        }

        if (errorMessage != null) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/main";
        }


        userAccountService.newTransfer(userSender.getId(), userRecipient.getId(), TransferSum);




        return "redirect:/main";
    }
}
