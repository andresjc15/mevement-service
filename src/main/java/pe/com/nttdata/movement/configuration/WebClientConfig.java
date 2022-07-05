package pe.com.nttdata.movement.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${config.endpoint.accounts}")
    private String uri;

    @Bean
    @LoadBalanced
    public WebClient.Builder registerWebClient() {
        return WebClient.builder().baseUrl(uri);
    }

}
