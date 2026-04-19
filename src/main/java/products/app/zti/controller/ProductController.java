package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import products.app.zti.model.User;
import products.app.zti.repository.FavouriteRepository;
import products.app.zti.repository.ProductRepository;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired private ProductRepository productRepository;
    @Autowired private FavouriteRepository favouriteRepository;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id).orElseThrow());
        return "product/show";
    }

    // DODANIE DO ULUBIONYCH (Odpowiednik Twojej metody z repo)
    @PostMapping("/{id}/favourite")
    public String toggleFavourite(@PathVariable Long id, User user) {
        // Logika szukania czy już jest w ulubionych i dodawania/usuwania
        // (Wymaga podpiętego użytkownika z sesji)
        return "redirect:/product/" + id;
    }
}