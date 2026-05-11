package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import products.app.zti.model.Product;
import products.app.zti.repository.ProductRepository;
import products.app.zti.repository.FavouriteRepository;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired private ProductRepository productRepository;
    @Autowired private FavouriteRepository favouriteRepository;

    @GetMapping("")
    public String index(Model model) {
        // Pobieramy produkty
        model.addAttribute("products", productRepository.findAll());
        return "product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        // pobranie produktu
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produkt nie istnieje"));

        //zmuszenie do wczytania
        if (product.getImages() != null) {
            product.getImages().size();
        }

        model.addAttribute("product", product);
        return "product/show";
    }

    @PostMapping("/{id}/favourite")
    public String toggleFavourite(@PathVariable Long id) {
        return "redirect:/product/" + id;
    }

    @GetMapping("/api/products/search")
    @ResponseBody // zwracamy JSON
    public List<ProductSearchDTO> searchApi(@RequestParam String query) {
        if (query.length() < 2) return List.of(); // Szukamy dopiero od 2 liter

        return productRepository.findTop8ByNameContainingIgnoreCase(query)
                .stream()
                .map(p -> new ProductSearchDTO(p.getId(), p.getName(), p.getImageUrl(), p.getPrice()))
                .toList();
    }

    // Prosty rekord pomocniczy
    public record ProductSearchDTO(Long id, String name, String imageUrl, Double price) {}

}