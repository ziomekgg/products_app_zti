package products.app.zti.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import products.app.zti.model.Product;
import products.app.zti.repository.CategoryRepository;
import products.app.zti.repository.ProductRepository;
import products.app.zti.service.FileStorageService;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private FileStorageService fileStorageService;

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
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam(value = "file", required = false) MultipartFile file) {

        // Sprawdzamy czy plik w ogóle przyszedł i czy nie jest pusty
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            product.setImageUrl(fileName);
        } else {
            // Jeśli nie ma zdjęcia, a to nowy produkt, możemy ustawić null
            // lub zostawić to, co przyszło z modelu (np. jeśli edytujemy)
            if (product.getId() == null) {
                product.setImageUrl(null);
            }
        }

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
    public String update(@PathVariable Long id,
                         @ModelAttribute Product product,
                         @RequestParam(value = "file", required = false) MultipartFile file) {

        // 1. Pobierz aktualną wersję produktu z bazy (MODUŁ MOCY)
        Product existingProduct = productRepository.findById(id).orElseThrow();

        // 2. Obsługa zdjęcia
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            product.setImageUrl(fileName);
        } else {
            // Jeśli nie wrzucono nowego pliku, zachowaj starą ścieżkę
            product.setImageUrl(existingProduct.getImageUrl());
        }

        product.setId(id);
        productRepository.save(product);
        return "redirect:/admin/product";
    }

}