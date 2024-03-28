package org.web.webauthorization.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web.webauthorization.BankData.Accounts.Accounts;
import org.web.webauthorization.BankData.Accounts.UserAccount;
import org.web.webauthorization.BankDataRepository.Accounts.AccountRepository;
import org.web.webauthorization.BankDataRepository.Accounts.UserAccountRepository;

@Controller
public class AccountActionController {

    private final AccountRepository accountRepository;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AccountActionController(AccountRepository accountRepository, UserAccountRepository userAccountRepository) {
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
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

        UserAccount isExist = userAccountRepository.findByAccountName(username);

        if(isExist == null){
            UserAccount newAccount = new UserAccount();
            newAccount.setAccountName(username);
            newAccount.setAccountPassword(password);

            do {
                newAccount.setCardNumber(UserAccount.generateCardNumber());
                isExist = accountRepository.findByCardNumber(newAccount.getCardNumber());

            } while (isExist != null);

            newAccount.setAccountRole(Accounts.AccountRole.USER);

            accountRepository.save(newAccount);
        } else {
            model.addAttribute("errorMessage", "Account with username " + username + " is already exist");
            return "signup";
        }

        return "redirect:login";
    }
}
