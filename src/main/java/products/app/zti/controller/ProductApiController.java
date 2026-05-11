package products.app.zti.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import products.app.zti.repository.ProductRepository;
import java.util.List;

@RestController // Automatycznie dodaje @ResponseBody do każdej metody
@RequestMapping("/api/products") // Czysty prefiks dla API
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductRepository productRepository;

    @GetMapping("/search")
    public List<ProductSearchDTO> search(@RequestParam(required = false) String query) {
        if (query == null || query.trim().length() < 2) return List.of();

        return productRepository.findTop8ByNameContainingIgnoreCase(query.trim())
                .stream()
                .map(p -> new ProductSearchDTO(p.getId(), p.getName(), p.getImageUrl(), p.getPrice()))
                .toList();
    }

    public record ProductSearchDTO(Long id, String name, String imageUrl, Double price) {}
}
