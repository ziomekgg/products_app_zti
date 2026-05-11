package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import products.app.zti.model.Category;
import products.app.zti.model.User;
import products.app.zti.repository.CategoryRepository;
import products.app.zti.repository.FavouriteRepository;
import products.app.zti.repository.ProductRepository;
import products.app.zti.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;

    // Widok wszystkich kategorii (kafelki)
    @GetMapping
    public String index(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/index";
    }

    // Widok produktów w konkretnej kategorii
    @GetMapping("/{id}")
    public String showCategory(@PathVariable Long id, Model model, Principal principal) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kategorii"));

        model.addAttribute("category", category);
        model.addAttribute("products", productRepository.findByCategoryId(id));

        // Logika dla serduszek
        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName()).get();
            List<Long> favIds = favouriteRepository.findByUserId(user.getId())
                    .stream().map(f -> f.getProduct().getId()).toList();
            model.addAttribute("favouriteProductIds", favIds);
        }

        return "category/show";
    }
}