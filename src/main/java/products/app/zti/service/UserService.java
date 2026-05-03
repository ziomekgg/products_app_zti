package products.app.zti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import products.app.zti.model.User;
import products.app.zti.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Użytkownik o tym adresie już istnieje w bazie.");
        }
        // Szyfrowanie hasła przed zapisem
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Nadanie domyślnych uprawnień
        user.setRoles("ROLE_USER");
        return userRepository.save(user);
    }
}
