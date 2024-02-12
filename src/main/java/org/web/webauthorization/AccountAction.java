package org.web.webauthorization;

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

    private final AccountRepository accountRepository;

    @Autowired
    public AccountAction(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/main")
    public String showMainBank() {
        return "main";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {

        Account isExist = accountRepository.findByAccountName(username);
        if(isExist != null){
            if(Objects.equals(password, isExist.getAccountPassword())){
                return "redirect:/succ";
            } else{
                model.addAttribute("errorMessage", "Incorrect username or password");
                return "main"; // возвращаем обратно страницу входа с сообщением об ошибке
            }
        }

        model.addAttribute("errorMessage", "Incorrect username or password");
        return "main"; // возвращаем обратно страницу входа с сообщением об ошибке
    }


    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration";
    }
    @PostMapping("/registration")
    public String registerUser(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            Model model) {

        Account isExist = accountRepository.findByAccountName(username);

        if(isExist == null){
            Account newAccount = new Account();
            newAccount.setAccountName(username);
            newAccount.setAccountPassword(password);

            accountRepository.save(newAccount);
        } else {
            model.addAttribute("errorMessage", "Account with username " + username + "is already exist");
            return "registration";
        }

        return "main";

    }



}
