package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import products.app.zti.model.Product;
import products.app.zti.repository.ProductRepository;
import products.app.zti.repository.FavouriteRepository;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired private ProductRepository productRepository;
    @Autowired private FavouriteRepository favouriteRepository;

    @GetMapping("")
    public String index(Model model) {
        // Pobieramy produkty - zdjęcia załadują się automatycznie w widoku
        // dzięki relacji OneToMany (domyślnie Lazy, ale w pętli zadziała)
        model.addAttribute("products", productRepository.findAll());
        return "product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        // 1. Pobierasz produkt
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produkt nie istnieje"));

        // 2. To jest ten "bezpiecznik" - dotykasz listy, żeby Hibernate ją pobrał teraz
        if (product.getImages() != null) {
            product.getImages().size();
        }

        model.addAttribute("product", product);
        return "product/show";
    }

    @PostMapping("/{id}/favourite")
    public String toggleFavourite(@PathVariable Long id) {
        // Tutaj w przyszłości dodasz logikę sprawdzającą zalogowanego Usera
        // Na razie zostawiamy redirect, żeby przycisk nie wywalał błędu 404
        return "redirect:/product/" + id;
    }
}