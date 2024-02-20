package org.web.webauthorization.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web.webauthorization.BankData.UserAccount;
import org.web.webauthorization.BankDataRepository.AccountRepository;

@Controller
public class AccountActionController {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    public AccountActionController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/")
    public String showIndexBank() {
        return "index";
    }
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails user) {
        if (user != null) {
            return "redirect:/main";
        }
        return "login";
    }
    @GetMapping("/signup")
    public String signup(@AuthenticationPrincipal UserDetails user) {
        if (user != null) {
            return "redirect:/main";
        }
        return "signup";
    }
    @PostMapping("/signup")
    public String signUpUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            Model model) {

        UserAccount isExist = accountRepository.findByAccountName(username);

        if(isExist == null){
            UserAccount newAccount = new UserAccount();
            newAccount.setAccountName(username);
            newAccount.setAccountPassword(password);

            isExist = new UserAccount();
            do {
                newAccount.setCardNumber(UserAccount.generateCardNumber());
                isExist = accountRepository.findByCardNumber(newAccount.getCardNumber());

            } while (isExist != null);

            accountRepository.save(newAccount);
        } else {
            model.addAttribute("errorMessage", "Account with username " + username + " is already exist");
            return "signup";
        }

        return "redirect:login";
    }
}
