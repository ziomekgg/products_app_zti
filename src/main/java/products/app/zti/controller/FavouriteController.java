package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import products.app.zti.repository.FavouriteRepository;

@Controller
@RequestMapping("/favourite")
public class FavouriteController {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @GetMapping("")
    public String index(Model model) {
        // Docelowo będziemy tu filtrować po zalogowanym użytkowniku
        model.addAttribute("favourites", favouriteRepository.findAll());
        return "favourite/index";
    }

    @PostMapping("/add/{productId}")
    public String add(@PathVariable Long productId) {
        // Logika dodawania do bazy
        return "redirect:/product";
    }
}
