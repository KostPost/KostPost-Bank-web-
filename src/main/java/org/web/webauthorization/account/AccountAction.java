package org.web.webauthorization.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.web.webauthorization.account.Account;
import org.web.webauthorization.account.AccountRepository;

import java.util.Objects;

@Controller
public class AccountAction {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    public AccountAction(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/")
    public String showIndexBank() {
        return "index";
    }
    @GetMapping("/main")
    public String showMainPage() {
        return "main";
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }
    @PostMapping("/signup")
    public String signUpUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            Model model) {

        Account isExist = accountRepository.findByAccountName(username);

        System.out.println("\n\n\n qwe \n\n\n");

        if(isExist == null){
            Account newAccount = new Account();
            newAccount.setAccountName(username);
            newAccount.setAccountPassword(password);

            accountRepository.save(newAccount);
        } else {
            model.addAttribute("errorMessage", "Account with username " + username + "is already exist");
            return "signup";
        }

        return "login";
    }
}
