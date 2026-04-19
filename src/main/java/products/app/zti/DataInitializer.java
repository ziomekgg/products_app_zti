package products.app.zti;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import products.app.zti.model.*;
import products.app.zti.repository.*;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Sprawdzamy czy baza jest pusta, żeby nie dodawać rekordów przy każdym restarcie
        if (categoryRepository.count() == 0) {

            // 2. Tworzymy Kategorie (odpowiednik Twoich kategorii z repo)
            Category electronics = new Category();
            electronics.setName("Elektronika");
            categoryRepository.save(electronics);

            Category clothes = new Category();
            clothes.setName("Odzież");
            categoryRepository.save(clothes);

            // 3. Tworzymy Produkty
            Product laptop = new Product();
            laptop.setName("Laptop Gamingowy");
            laptop.setPrice(4500.00);
            laptop.setDescription("Super szybki laptop do gier i pracy.");
            laptop.setCategory(electronics);
            laptop.setStockQuantity(15); // Twoje nowe pole!
            productRepository.save(laptop);

            Product tshirt = new Product();
            tshirt.setName("Koszulka Bawełniana");
            tshirt.setPrice(59.99);
            tshirt.setDescription("Wygodna koszulka z logo projektu.");
            tshirt.setCategory(clothes);
            tshirt.setStockQuantity(50);
            productRepository.save(tshirt);

            // 4. Tworzymy Użytkownika (Testowy Admin i User)
            User admin = new User();
            admin.setEmail("admin@test.pl");
            admin.setPassword("admin123"); // Na razie bez kodowania, dopóki nie mamy Security
            admin.setRoles("ROLE_ADMIN");
            userRepository.save(admin);

            System.out.println(">> DataMock: Rekordy zostały dodane do bazy!");
        } else {
            System.out.println(">> DataMock: Dane już istnieją w bazie, pomijam inicjalizację.");
        }
    }
}