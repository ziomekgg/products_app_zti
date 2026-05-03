package products.app.zti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import products.app.zti.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}