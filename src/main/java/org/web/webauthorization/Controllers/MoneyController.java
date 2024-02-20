package org.web.webauthorization.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;


@Controller
public class MoneyController {

    @GetMapping("/main")
    public String showMainPage() {
        return "main";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("card-number") String cardNumber,
                           @RequestParam("amount") BigDecimal amount,
                           @RequestParam("comment") String comment) {

        System.out.println("\n\n" + cardNumber + "\t" + amount + "\t" + comment + "\n\n");

        return "redirect:main";
    }
}
