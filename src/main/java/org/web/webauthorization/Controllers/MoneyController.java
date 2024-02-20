package org.web.webauthorization.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.UserAccountRepository;

import java.math.BigDecimal;

import org.springframework.ui.Model;

@Controller
public class MoneyController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("/main")
    public String showMainPage() {
        return "main";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("card-number") String cardNumber,
                           @RequestParam("amount") BigDecimal amount,
                           @RequestParam("comment") String comment,
                           RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        UserAccount userSender = userAccountRepository.findByAccountName(currentPrincipalName);
        UserAccount userRecipient = userAccountRepository.findByCardNumber(cardNumber);

        String errorMessage = null;
        if (userRecipient == null) {
            //errorMessage = "No recipient found with the provided card number.";
        }
        if (cardNumber.length() != 19) {
            errorMessage = "Card number must be exactly 19 digits long.";
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            errorMessage = "Transfer amount must be positive and greater than zero.";
        } else if (amount.compareTo(userSender.getAccountBalance()) > 0) {
            errorMessage = "Insufficient funds for this transfer.";
        }

        if (errorMessage != null) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/main";
        }

        return "redirect:/main";
    }
}
