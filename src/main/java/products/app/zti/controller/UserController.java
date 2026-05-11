package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import products.app.zti.model.Reservation;
import products.app.zti.model.User;
import products.app.zti.repository.ReservationRepository;
import products.app.zti.repository.UserRepository;
import products.app.zti.service.UserService;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    // Uniwersalna metoda do wyciągania maila
    private String getEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken token) {
            return token.getPrincipal().getAttribute("email");
        }
        return principal.getName();
    }

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = getEmailFromPrincipal(principal);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        List<Reservation> orders = reservationRepository.findByUserIdAndStatus(user.getId(), "ORDERED");

        // Sprawdzamy, czy to użytkownik Google
        boolean isGoogleUser = principal instanceof OAuth2AuthenticationToken;

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("isGoogleUser", isGoogleUser); // Przekazujemy info do HTML
        return "profile";
    }

    @PostMapping("/update-email")
    public String updateEmail(@RequestParam String email, Principal principal) {
        // Blokada dla Google
        if (principal instanceof OAuth2AuthenticationToken) {
            return "redirect:/profile?error=oauth_restricted";
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        userService.updateEmail(user, email);
        return "redirect:/profile?success=email";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String newPassword, Principal principal) {
        // Blokada dla Google
        if (principal instanceof OAuth2AuthenticationToken) {
            return "redirect:/profile?error=oauth_restricted";
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        userService.updatePassword(user, newPassword);
        return "redirect:/profile?success=password";
    }
}