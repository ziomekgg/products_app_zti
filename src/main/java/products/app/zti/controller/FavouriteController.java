package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken; // DODAJ TO
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import products.app.zti.model.Favourite;
import products.app.zti.model.User;
import products.app.zti.repository.FavouriteRepository;
import products.app.zti.repository.ProductRepository;
import products.app.zti.repository.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/favourite")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Uniwersalna metoda do wyciągania maila
    private String getEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken token) {
            return token.getPrincipal().getAttribute("email");
        }
        return principal.getName();
    }

    @GetMapping
    public String index(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = getEmailFromPrincipal(principal);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        model.addAttribute("favourites", favouriteRepository.findByUserId(user.getId()));
        return "favourite/index";
    }

    @PostMapping("/toggle/{productId}")
    public String toggle(@PathVariable Long productId, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = getEmailFromPrincipal(principal);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        Optional<Favourite> existing = favouriteRepository.findByUserIdAndProductId(user.getId(), productId);

        if (existing.isPresent()) {
            favouriteRepository.delete(existing.get());
        } else {
            Favourite fav = new Favourite();
            fav.setUser(user);
            fav.setProduct(productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produkt nie istnieje")));
            favouriteRepository.save(fav);
        }

        return "redirect:/product";
    }
}