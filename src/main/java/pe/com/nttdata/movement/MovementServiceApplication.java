package pe.com.nttdata.movement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "${swagger.title}",
				version = "${swagger.version}",
				description = "${swagger.description}"))
public class MovementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovementServiceApplication.class, args);
	}

}
