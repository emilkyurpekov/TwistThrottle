package twistthrottle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class TwistThrottleApplication {

    public static void main(String[] args)  {
        SpringApplication.run(TwistThrottleApplication.class, args);
    }

}
