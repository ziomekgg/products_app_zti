package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import products.app.zti.repository.ImageRepository;

@Controller
@RequestMapping("/admin/image")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        imageRepository.deleteById(id);
        return "redirect:/admin/product"; // wracamy do listy produktów
    }
}