package products.app.zti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import products.app.zti.service.ReservationService;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/make/{productId}")
    public String makeReservation(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            // Tu docelowo przekażemy zalogowanego Usera
            reservationService.reserveProduct(productId, null, quantity);
            return "redirect:/product/" + productId + "?success";
        } catch (Exception e) {
            return "redirect:/product/" + productId + "?error=" + e.getMessage();
        }
    }
}