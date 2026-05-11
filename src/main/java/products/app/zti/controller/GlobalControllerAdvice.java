package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import products.app.zti.repository.FavouriteRepository;
import products.app.zti.repository.ReservationRepository;
import products.app.zti.repository.UserRepository;
import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @ModelAttribute("favCount")
    public long getFavouriteCount(Principal principal) {
        if (principal == null) return 0; // Jeśli nie ma operatora, licznik = 0

        return userRepository.findByEmail(principal.getName())
                .map(user -> favouriteRepository.countByUserId(user.getId()))
                .orElse(0L);
    }

    @ModelAttribute
    public void addCartCount(Model model, Principal principal) {
        int reservationCount = 0;
        if (principal != null) {
            userRepository.findByEmail(principal.getName()).ifPresent(user -> {
                // Liczymy sumę ilości wszystkich zarezerwowanych przedmiotów
                int count = reservationRepository.findByUserId(user.getId())
                        .stream()
                        .mapToInt(r -> r.getQuantity())
                        .sum();
                model.addAttribute("resCount", count);
            });
        } else {
            model.addAttribute("resCount", 0);
        }
    }
}