package products.app.zti.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    private String imageUrl; // Odpowiednik pola z Twoich widoków Twig

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    // NOWE POLE Z ZADANIA:
    private int stockQuantity;
}