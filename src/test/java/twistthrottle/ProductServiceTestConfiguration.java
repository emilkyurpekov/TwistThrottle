package twistthrottle;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import twistthrottle.repositories.MotorcycleRepository;
import twistthrottle.repositories.ProductRepository;

import static org.mockito.Mockito.mock;

@TestConfiguration
@ComponentScan(basePackages = "twistthrottle", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { MotorcycleRepository.class, // Example of excluding another repository
}))

    public class ProductServiceTestConfiguration {
        @Bean
        @Primary
        public ProductRepository productRepository() {
            return mock(ProductRepository.class);
        }
}
