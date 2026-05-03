package products.app.zti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Wyłączone zgodnie z Twoim kodem
                .authorizeHttpRequests(auth -> auth
                        // 1. OTWARTE DRZWI
                        .requestMatchers(
                                "/", "/product/**", "/login", "/register", "/uploads/**", "/css/**", "/js/**",
                                "/regulamin", "/polityka-prywatnosci", "/cookies", "/kontakt", "/reklamacje", "/faq" // DODAJ TO
                        ).permitAll()

                        // 2. ZAMKNIĘTE DRZWI (Admin)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 3. Reszta wymaga logowania
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/product", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Adres, pod który uderza formularz z layoutu
                        .logoutSuccessUrl("/") // Gdzie wyrzucić użytkownika po wylogowaniu
                        .invalidateHttpSession(true) // Całkowite wyczyszczenie sesji
                        .deleteCookies("JSESSIONID") // Usunięcie ciasteczka sesyjnego
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Na początek użyjemy NoOp (brak szyfrowania), żebyś mógł się zalogować danymi z DataMock
        // Potem zmienimy to na BCrypt (tak jak miałeś w Symfony)
        return NoOpPasswordEncoder.getInstance();
    }

}