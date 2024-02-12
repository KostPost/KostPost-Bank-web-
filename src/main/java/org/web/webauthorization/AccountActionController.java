//package org.web.webauthorization;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.web.webauthorization.account.Account;
//import org.web.webauthorization.account.AccountRepository;
//
//@Controller
//public class AccountActionController {
//
//    private final AccountRepository accountRepository;
//
//    @Autowired
//    public AccountActionController(AccountRepository accountRepository) {
//        this.accountRepository = accountRepository;
//    }
//
//
//
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }
//
//    @PostMapping("/login")
//    public String validateLogin(
//            @RequestParam(name = "username") String username,
//            @RequestParam(name = "password") String password,
//            Model model) {
//
//        Account IsCorrectAccount = accountRepository.findByAccountName(username);
//
//        if (IsCorrectAccount != null) {
//            if (password.equals(IsCorrectAccount.getAccountPassword())) {
//                return "redirect:/successfullylogin";
//            }
//        }
//
//        model.addAttribute("error", "Invalid login credentials. Please try again.");
//        return "login";
//    }
//
//    @GetMapping("/successfullylogin")
//    public String showSuccessfullyLoginPage() {
//        return "successfullylogin";
//    }
//
//
//
//
//

//
//
//
//
//}
