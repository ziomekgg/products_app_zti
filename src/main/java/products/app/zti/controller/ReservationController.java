package products.app.zti.controller;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
@RequiredArgsConstructor // Użyj Lombok zamiast @Autowired dla czystości
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @GetMapping
    public String index(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName()).get();
        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());

        // Obliczamy sumę zamówienia
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
            User user = userRepository.findByEmail(principal.getName()).get();
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
        User user = userRepository.findByEmail(principal.getName()).get();
        List<Reservation> reservations = reservationRepository.findByUserId(user.getId());

        if (reservations.isEmpty()) return "redirect:/product";

        // 1. Wysyłamy e-mail (asynchronicznie najlepiej, ale tu zrobimy prosto)
        emailService.sendConfirmationEmail(user.getEmail(), storeLocation, reservations);

        // 2. Czyścimy rezerwacje (użytkownik już je "zamówił")
        // UWAGA: Stan magazynowy został już zmniejszony przy dodawaniu do koszyka!
        reservationRepository.deleteAll(reservations);

        return "redirect:/?success_order";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage() {
        // Tu nic nie zapisujemy w bazie.
        // Po prostu wysyłamy użytkownika do widoku z mapą.
        return "reservation/checkout";
    }
}