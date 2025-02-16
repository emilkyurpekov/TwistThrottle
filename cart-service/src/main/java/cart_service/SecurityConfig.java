package cart_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/cart/**").permitAll() // ✅ Allow public access to /api/cart
                        .anyRequest().authenticated() // Require authentication for other endpoints
                )
                .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF for APIs
                .formLogin(login -> login.disable()) // ✅ Disable login form
                .httpBasic(basic -> basic.disable()); // ✅ Disable HTTP Basic authentication

        return http.build();
    }
}
