package products.app.zti.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import products.app.zti.service.Custom0Auth2UserService; // DODAJ TO

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // To nam automatycznie wstrzyknie CustomOAuth2UserService
public class SecurityConfig {

    private final Custom0Auth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/api/products/**", "/product/**", "/category/**",
                                "/login", "/register", "/uploads/**", "/css/**", "/js/**",
                                "/oauth2/**", // DODAJ TO: ścieżki autoryzacji OAuth
                                "/regulamin", "/polityka-prywatnosci", "/cookies", "/kontakt", "/reklamacje", "/faq"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/product", true)
                        .permitAll()
                )
                // --- TU BYŁA PRZERWA W OBWODZIE - DODAJEMY TO: ---
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // Używamy Twojej strony logowania
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Podpinamy zapis do bazy
                        )
                        .defaultSuccessUrl("/product", true) // Gdzie po sukcesie
                )
                // --------------------------------------------------
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOp to "zworka" zamiast bezpiecznika - dobra do testów,
        // ale przed obroną na AGH zmienimy to na BCrypt!
        return NoOpPasswordEncoder.getInstance();
    }
}