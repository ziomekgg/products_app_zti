package products.app.zti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import products.app.zti.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}