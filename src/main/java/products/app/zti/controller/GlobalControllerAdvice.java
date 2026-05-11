package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken; // DODAJ TO
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

    // Uniwersalna metoda do wyciągania maila z Principal
    private String getEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken token) {
            return token.getPrincipal().getAttribute("email");
        }
        return principal.getName();
    }

    @ModelAttribute("favCount")
    public long getFavouriteCount(Principal principal) {
        if (principal == null) return 0;

        String email = getEmailFromPrincipal(principal);

        return userRepository.findByEmail(email)
                .map(user -> favouriteRepository.countByUserId(user.getId()))
                .orElse(0L);
    }

    @ModelAttribute
    public void addCartCount(Model model, Principal principal) {
        if (principal != null) {
            String email = getEmailFromPrincipal(principal);

            userRepository.findByEmail(email).ifPresent(user -> {
                // Liczymy tylko rezerwacje ze statusem IN_CART (czyli te w koszyku)
                int count = reservationRepository.findByUserIdAndStatus(user.getId(), "IN_CART")
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