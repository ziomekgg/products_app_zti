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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. OTWARTE DRZWI (Każdy widzi sklep i obrazki)
                        .requestMatchers("/", "/product/**", "/login", "/css/**", "/js/**").permitAll()

                        // 2. ZAMKNIĘTE DRZWI (Tylko zalogowany Admin)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 3. Reszta (np. rezerwacje, ulubione) wymaga logowania
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/product", true)
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