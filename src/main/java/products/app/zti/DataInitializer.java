package products.app.zti;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import products.app.zti.model.*;
import products.app.zti.repository.*;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Ścieżka do Twojego folderu uploads
    private final String UPLOAD_DIR = "uploads/";

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            // Tworzymy folder, jeśli nie istnieje
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // --- KATEGORIE ---
            Category cpu = createCategory("Procesory");
            Category gpu = createCategory("Karty Graficzne");
            Category ram = createCategory("Pamięci RAM");

            // --- PRODUKTY Z POBIERANIEM ZDJĘĆ ---
            createProduct("Intel Core i9-14900K", 2899.00, cpu, 10,
                    "https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?q=80&w=800");

            createProduct("AMD Ryzen 7 7800X3D", 1749.00, cpu, 25,
                    "https://images.unsplash.com/photo-1555617766-c94804975da3?q=80&w=800");

            createProduct("NVIDIA RTX 4080 Super", 4899.00, gpu, 5,
                    "https://images.unsplash.com/photo-1587202395167-93e1858f96e4?q=80&w=800");

            // --- 1. DODATKOWE KATEGORIE ---
            Category mobo = createCategory("Płyty Główne");
            Category psu = createCategory("Zasilacze");
            Category cooling = createCategory("Chłodzenie");
            Category ssd = createCategory("Dyski");
            Category cases = createCategory("Obudowy");

// --- 2. PROCESORY (CPU) ---
            createProduct("Intel Core i7-14700K", 1850.00, cpu, 15, "https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?q=80&w=800");
            createProduct("AMD Ryzen 5 7600X", 950.00, cpu, 30, "https://images.unsplash.com/photo-1555617766-c94804975da3?q=80&w=800");
            createProduct("Intel Core i5-13600K", 1300.00, cpu, 20, "https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?q=80&w=801");
            createProduct("AMD Ryzen 9 7950X", 2500.00, cpu, 10, "https://images.unsplash.com/photo-1555617766-c94804975da4?q=80&w=800");

// --- 3. KARTY GRAFICZNE (GPU) ---
            createProduct("NVIDIA GeForce RTX 4090", 8999.00, gpu, 3, "https://images.unsplash.com/photo-1591488320449-011701bb6704?q=80&w=800");
            createProduct("AMD Radeon RX 7900 XTX", 4400.00, gpu, 7, "https://images.unsplash.com/photo-1587202395167-93e1858f96e4?q=80&w=801");
            createProduct("NVIDIA GeForce RTX 4060 Ti", 1750.00, gpu, 20, "https://images.unsplash.com/photo-1591488320449-011701bb6705?q=80&w=800");
            createProduct("MSI RTX 4070 Ventus", 2600.00, gpu, 12, "https://images.unsplash.com/photo-1587202395167-93e1858f96e4?q=80&w=802");

// --- 4. PAMIĘCI RAM ---
            createProduct("Kingston FURY Beast 16GB DDR5", 310.00, ram, 50, "https://images.unsplash.com/photo-1541029071515-84cc54f84dc5?q=80&w=801");
            createProduct("Lexar Thor 32GB DDR4", 290.00, ram, 45, "https://images.unsplash.com/photo-1562976540-1502c2145186?q=80&w=801");
            createProduct("Crucial Pro 32GB DDR5", 480.00, ram, 25, "https://images.unsplash.com/photo-1541029071515-84cc54f84dc5?q=80&w=802");
            createProduct("Patriot Viper Venom 16GB", 280.00, ram, 60, "https://images.unsplash.com/photo-1562976540-1502c2145186?q=80&w=802");

// --- 5. DYSKI SSD ---
            createProduct("Crucial T700 1TB Gen5", 790.00, ssd, 8, "https://images.unsplash.com/photo-1597872200370-493dc2393556?q=80&w=801");
            createProduct("Kingston NV2 2TB", 450.00, ssd, 80, "https://images.unsplash.com/photo-1628557118391-56cd72d61081?q=80&w=801");
            createProduct("WD Black SN850X 1TB", 390.00, ssd, 40, "https://images.unsplash.com/photo-1597872200370-493dc2393556?q=80&w=802");
            createProduct("ADATA Legend 960 2TB", 620.00, ssd, 35, "https://images.unsplash.com/photo-1628557118391-56cd72d61081?q=80&w=802");

// --- 6. PŁYTY GŁÓWNE (MOBO) ---
            createProduct("ASUS TUF Gaming Z790-Plus", 1250.00, mobo, 15, "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=800");
            createProduct("Gigabyte B650 Eagle AX", 740.00, mobo, 22, "https://images.unsplash.com/photo-1550745165-9bc0b252726f?q=80&w=800");
            createProduct("MSI MAG B760 Tomahawk", 890.00, mobo, 18, "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=801");
            createProduct("ASRock X670E Taichi", 2100.00, mobo, 5, "https://images.unsplash.com/photo-1550745165-9bc0b252726f?q=80&w=801");

// --- 7. ZASILACZE (PSU) ---
            createProduct("Corsair RM850x 850W Gold", 650.00, psu, 20, "https://images.unsplash.com/photo-1591489378430-ef2f4c626b35?q=80&w=800");
            createProduct("be quiet! Straight Power 12 1000W", 890.00, psu, 10, "https://images.unsplash.com/photo-1591489378430-ef2f4c626b35?q=80&w=801");
            createProduct("Endorfy Vero L5 600W Bronze", 250.00, psu, 50, "https://images.unsplash.com/photo-1591489378430-ef2f4c626b35?q=80&w=802");
            createProduct("Seasonic Focus GX-750", 520.00, psu, 30, "https://images.unsplash.com/photo-1591489378430-ef2f4c626b35?q=80&w=803");

// --- 8. CHŁODZENIE ---
            createProduct("Endorfy Navis F360 AIO", 480.00, cooling, 15, "https://images.unsplash.com/photo-1563770660941-20978e87081b?q=80&w=800");
            createProduct("Noctua NH-D15 chromax.black", 550.00, cooling, 10, "https://images.unsplash.com/photo-1563770660941-20978e87081b?q=80&w=801");
            createProduct("be quiet! Dark Rock Pro 5", 420.00, cooling, 20, "https://images.unsplash.com/photo-1563770660941-20978e87081b?q=80&w=802");
            createProduct("Arctic Liquid Freezer III 360", 410.00, cooling, 25, "https://images.unsplash.com/photo-1563770660941-20978e87081b?q=80&w=803");

// --- 9. OBUDOWY ---
            createProduct("Fractal Design North", 720.00, cases, 12, "https://images.unsplash.com/photo-1587202395103-12502395167?q=80&w=802");
            createProduct("Corsair 4000D Airflow", 410.00, cases, 30, "https://images.unsplash.com/photo-1649231835687-f8319f63567d?q=80&w=801");

            // --- USER ---
            User admin = new User();
            admin.setEmail("operator@hardware.store");
            admin.setPassword("admin123");
            admin.setRoles("ROLE_ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);

            System.out.println(">> [DataMock]: Wszystkie zdjęcia zostały pobrane do folderu uploads!");
        }
    }

    private Category createCategory(String name) {
        Category cat = new Category();
        cat.setName(name);
        return categoryRepository.save(cat);
    }

    private void createProduct(String name, double price, Category cat, int stock, String remoteUrl) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCategory(cat);
        p.setStockQuantity(stock);
        p.setDescription("Wysokiej jakości podzespół dla profesjonalistów.");

        // Pobieramy zdjęcie i zapisujemy lokalnie
        String localFileName = downloadImage(remoteUrl);
        p.setImageUrl(localFileName); // W bazie zapisujemy tylko nazwę pliku (np. "a1b2c3.jpg")

        productRepository.save(p);
    }

    private String downloadImage(String urlString) {
        try {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            Path targetPath = Paths.get(UPLOAD_DIR + fileName);

            URL url = new URL(urlString);
            try (InputStream in = url.openStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileName;
        } catch (Exception e) {
            System.err.println("Błąd pobierania zdjęcia: " + e.getMessage());
            return "default.jpg"; // Zabezpieczenie
        }
    }
}