package products.app.zti.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import products.app.zti.model.Image;
import products.app.zti.model.Product;
import products.app.zti.repository.CategoryRepository;
import products.app.zti.repository.ProductRepository;
import products.app.zti.repository.ImageRepository;
import products.app.zti.service.FileStorageService;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private FileStorageService fileStorageService;
    @Autowired private ImageRepository imageRepository;

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
                              @RequestParam(value = "files", required = false) MultipartFile[] files) {

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = fileStorageService.storeFile(file);

                    // Tworzymy rekord w nowym MODULE MOCY (tabela Image)
                    Image img = new Image();
                    img.setUrl(fileName);
                    product.addImage(img); // Nasz helper spina relację

                    // Tymczasowy fallback: jeśli produkt nie ma jeszcze imageUrl,
                    // ustawiamy pierwszy wgrany plik jako główny
                    if (product.getImageUrl() == null) {
                        product.setImageUrl(fileName);
                        img.setMain(true);
                    }
                }
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
        Product product = productRepository.findById(id).orElseThrow();

        // 1. Czyścimy pliki z nowej galerii
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            for (Image img : product.getImages()) {
                fileStorageService.deleteFile(img.getUrl());
            }
        }

        // 2. Czyścimy stare zdjęcie główne (póki istnieje pole imageUrl)
        if (product.getImageUrl() != null) {
            fileStorageService.deleteFile(product.getImageUrl());
        }

        // 3. Usuwamy z bazy (CascadeType.ALL usunie rekordy w tabeli Image)
        productRepository.delete(product);

        return "redirect:/admin/product";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute Product product,
                         @RequestParam(value = "files", required = false) MultipartFile[] files) {

        // 1. Pobieramy istniejący produkt (MODUŁ MOCY)
        Product existingProduct = productRepository.findById(id).orElseThrow();

        // 2. Aktualizujemy podstawowe dane
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        // imageUrl na razie zostawiamy w spokoju, żeby widoki działały

        // 3. Obsługa wielu nowych zdjęć
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // Zapisujemy plik na dysku przez Twój serwis
                    String fileName = fileStorageService.storeFile(file);

                    // Tworzymy obiekt nowej encji Image
                    Image newImage = new Image();
                    newImage.setUrl(fileName);

                    // Używamy helpera addImage, który spina relację
                    existingProduct.addImage(newImage);
                }
            }
        }

        // 4. Zapisujemy wszystko (CascadeType.ALL zadba o zapisanie nowych obiektów Image)
        productRepository.save(existingProduct);

        return "redirect:/admin/product/" + id + "/edit";
    }
    @PostMapping("/{id}/delete-image")
    public String deleteImage(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();

        // Usuwamy fizyczny plik z folderu uploads
        if (product.getImageUrl() != null) {
            fileStorageService.deleteFile(product.getImageUrl());
        }

        // Czyścimy wpis w bazie danych
        product.setImageUrl(null);
        productRepository.save(product);

        return "redirect:/admin/product/" + id + "/edit";
    }

    @PostMapping("/image/set-main/{imageId}")
    public String setMainImage(@PathVariable Long imageId) {
        // 1. Pobieramy zdjęcie
        Image selectedImage = this.imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid image Id:" + imageId));

        Product product = selectedImage.getProduct();

        // 2. Logika "MODUŁU MOCY": tylko jedno zdjęcie może być główne
        product.getImages().forEach(img -> {
            img.setMain(img.getId().equals(imageId));
        });

        // 3. Synchronizacja ze starym polem imageUrl
        product.setImageUrl(selectedImage.getUrl());

        // 4. Zapisujemy zmiany
        productRepository.save(product);

        return "redirect:/admin/product/" + product.getId() + "/edit";
    }

}