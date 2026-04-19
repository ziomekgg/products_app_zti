package products.app.zti.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import products.app.zti.model.Product;
import products.app.zti.repository.CategoryRepository;
import products.app.zti.repository.ProductRepository;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    // LISTA (index)
    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product/index";
    }

    // DODAWANIE (new) - Widok
    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product/new";
    }

    // DODAWANIE (new) - Akcja
    @PostMapping("/new")
    public String saveProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/admin/product";
    }

    // EDYCJA (edit) - Widok
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product/edit";
    }

    // USUWANIE (delete)
    @PostMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/product";
    }

    // EDYCJA (edit) - Akcja zapisu
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute Product product) {
        // Musimy upewnić się, że edytujemy istniejący produkt, a nie tworzymy nowy
        product.setId(id);
        productRepository.save(product);
        return "redirect:/admin/product";
    }

}