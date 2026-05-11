package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public String index(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();
        model.addAttribute("favourites", favouriteRepository.findByUserId(user.getId()));
        return "favourite/index";
    }

    @PostMapping("/toggle/{productId}")
    public String toggle(@PathVariable Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).get();

        Optional<Favourite> existing = favouriteRepository.findByUserIdAndProductId(user.getId(), productId);

        if (existing.isPresent()) {
            favouriteRepository.delete(existing.get());
        } else {
            Favourite fav = new Favourite();
            fav.setUser(user);
            fav.setProduct(productRepository.findById(productId).get());
            favouriteRepository.save(fav);
        }

        return "redirect:/product";
    }
}
