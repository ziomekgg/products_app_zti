package products.app.zti.controller;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken; // DODAJ TO
import products.app.zti.model.Reservation;
import products.app.zti.model.User;
import products.app.zti.repository.ReservationRepository;
import products.app.zti.repository.UserRepository;
import products.app.zti.service.ReservationService;
import products.app.zti.service.EmailService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // POMOCNICZA METODA DO WYCIĄGANIA MAILA (NASZ KONWERTER)
    private String getEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken token) {
            // Logika dla Google - wyciągamy atrybut "email"
            return token.getPrincipal().getAttribute("email");
        }
        // Logika dla zwykłego formularza
        return principal.getName();
    }

    @GetMapping
    public String index(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = getEmailFromPrincipal(principal);
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Reservation> reservations = reservationRepository.findByUserIdAndStatus(user.getId(), "IN_CART");

        double total = reservations.stream()
                .mapToDouble(r -> r.getProduct().getPrice() * r.getQuantity())
                .sum();

        model.addAttribute("reservations", reservations);
        model.addAttribute("totalPrice", total);
        return "reservation/index";
    }

    @PostMapping("/make/{productId}")
    public String makeReservation(@PathVariable Long productId,
                                  @RequestParam(defaultValue = "1") int quantity,
                                  Principal principal) {
        if (principal == null) return "redirect:/login";

        try {
            String email = getEmailFromPrincipal(principal);
            User user = userRepository.findByEmail(email).orElseThrow();
            reservationService.reserveProduct(productId, user, quantity);
            return "redirect:/reservation?success";
        } catch (Exception e) {
            return "redirect:/product/" + productId + "?error=" + e.getMessage();
        }
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "redirect:/reservation?removed";
    }

    @PostMapping("/finalize")
    public String finalizeReservation(@RequestParam String storeLocation, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = getEmailFromPrincipal(principal);
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Reservation> reservations = reservationRepository.findByUserIdAndStatus(user.getId(), "IN_CART");

        if (reservations.isEmpty()) return "redirect:/product";

        emailService.sendConfirmationEmail(user.getEmail(), storeLocation, reservations);

        for (Reservation res : reservations) {
            res.setStatus("ORDERED");
            res.setStoreLocation(storeLocation);
        }
        reservationRepository.saveAll(reservations);

        return "redirect:/?success_order";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage() {
        return "reservation/checkout";
    }
}