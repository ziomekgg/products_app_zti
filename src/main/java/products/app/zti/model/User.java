package products.app.zti.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Przechowujemy role jako jeden String oddzielony przecinkami,
    // np. "ROLE_USER,ROLE_ADMIN", co ułatwia zapis w bazie.
    private String roles;

    // Pomocnicza metoda do pobierania ról jako Listy (ułatwia pracę w Javie)
    public List<String> getRoleList() {
        if (this.roles == null || this.roles.isEmpty()) {
            return List.of("ROLE_USER"); // Domyślna rola
        }
        return Arrays.asList(this.roles.split(","));
    }
}