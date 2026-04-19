package products.app.zti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import products.app.zti.model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Odpowiednik findBy... z Symfony
    List<Product> findByCategoryId(Long categoryId);
}