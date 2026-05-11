package products.app.zti.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import products.app.zti.model.User;
import products.app.zti.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Custom0Auth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Logika inżynierska: sprawdź czy użytkownik już istnieje
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // Jeśli nie ma go w bazie - rejestrujemy go automatycznie
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(""); // OAuth nie potrzebuje hasła w naszej bazie
            newUser.setEnabled(true);
            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}
