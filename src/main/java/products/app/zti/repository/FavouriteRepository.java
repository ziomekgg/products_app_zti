package products.app.zti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import products.app.zti.model.Favourite;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findByUserId(Long userId);
    // usuwanie i sprawdzanie duplikatów
    Optional<Favourite> findByUserIdAndProductId(Long userId, Long productId);
    long countByUserId(Long userId);
}