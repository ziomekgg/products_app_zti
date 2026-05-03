package products.app.zti.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setProduct(this);
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    // NOWE POLE Z ZADANIA:
    private int stockQuantity;
}