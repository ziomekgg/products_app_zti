package products.app.zti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String login() {
        return "security/login";
    }

    // W Spring Security wylogowanie jest obsługiwane automatycznie,
    // ale możemy zdefiniować trasę, jeśli chcemy specyficzny widok.
}