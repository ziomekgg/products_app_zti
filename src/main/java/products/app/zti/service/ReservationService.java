package products.app.zti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import products.app.zti.model.Product;
import products.app.zti.model.Reservation;
import products.app.zti.model.User;
import products.app.zti.repository.ProductRepository;
import products.app.zti.repository.ReservationRepository;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public void reserveProduct(Long productId, User user, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nie istnieje"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Niewystarczająca ilość w magazynie!");
        }

        // Zmniejszamy stan magazynowy (Kluczowe zadanie!)
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // Zapisujemy rezerwację
        Reservation reservation = new Reservation();
        reservation.setProduct(product);
        reservation.setUser(user);
        reservation.setQuantity(quantity);
        reservation.setCreatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }
}