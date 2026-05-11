package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import products.app.zti.model.Category;
import products.app.zti.repository.CategoryRepository;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/category/index"; // Sprawdź czy ta ścieżka IDEALNIE pasuje do pliku
    }

    // Widok formularza
    @GetMapping("/new")
    public String newCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/new";
    }

    // Przyjmowanie obiektu
    @PostMapping("/new")
    public String saveCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        if (categoryRepository.existsByNameIgnoreCase(name.trim())) {
            // Jeśli istnieje, wysyłamy sygnał błędu do widoku
            redirectAttributes.addFlashAttribute("errorMessage", "Kategoria o nazwie '" + name + "' już istnieje!");
            return "redirect:/admin/category/new";
        }

        Category category = new Category();
        category.setName(name.trim());
        categoryRepository.save(category);

        redirectAttributes.addFlashAttribute("successMessage", "Segment '" + name + "' zainstalowany poprawnie.");
        return "redirect:/admin/category";
    }

    // edycja kategorii
    @GetMapping("/{id}/edit")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow();
        model.addAttribute("category", category);
        return "admin/category/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute Category category,
                                 RedirectAttributes redirectAttributes) {

        String newName = category.getName().trim();

        // Sprawdzamy, czy inna kategoria (ID != id) ma już taką nazwę
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(newName, id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Przerwanie operacji: Nazwa '" + newName + "' jest już zarezerwowana dla innego segmentu!");
            return "redirect:/admin/category/" + id + "/edit";
        }

        category.setId(id);
        category.setName(newName);
        categoryRepository.save(category);

        redirectAttributes.addFlashAttribute("successMessage", "Parametry segmentu '" + newName + "' zaktualizowane.");
        return "redirect:/admin/category";
    }

    @PostMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/category";
    }
}