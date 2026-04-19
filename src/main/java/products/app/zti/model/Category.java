package products.app.zti.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Relacja: jedna kategoria ma wiele produktów
    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    @JsonIgnore
    private List<Product> products;
}