package twistthrottle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // Add this
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCrypt
import org.springframework.security.crypto.password.PasswordEncoder;   // Import PasswordEncoder
import org.springframework.security.web.SecurityFilterChain;
// Make sure this import matches the location where you created CustomUserDetailsService
import twistthrottle.services.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*.css", "/js/**", "/images/**", "/webjars/**").permitAll()

                        .requestMatchers("/home", "/login", "/register", "/about", "/public/**").permitAll()
                        .requestMatchers("/categories", "/products").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/profile/**", "/cart/**", "/orderConfirmation", "/addMotorcycle", "/motorcycles").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }
}