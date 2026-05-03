package products.app.zti.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private boolean main;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}