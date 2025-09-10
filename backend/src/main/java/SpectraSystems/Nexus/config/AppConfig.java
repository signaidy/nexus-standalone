package SpectraSystems.Nexus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    
    /** 
     * @return returns a 'RestTemplate'
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

