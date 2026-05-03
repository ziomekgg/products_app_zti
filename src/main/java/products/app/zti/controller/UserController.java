package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import products.app.zti.model.User;
import products.app.zti.repository.UserRepository;
import products.app.zti.service.UserService;
import java.security.Principal;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@RequestParam String email, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        userService.updateEmail(user, email);
        return "redirect:/profile?success=email";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String newPassword, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        userService.updatePassword(user, newPassword);
        return "redirect:/profile?success=password";
    }
}
