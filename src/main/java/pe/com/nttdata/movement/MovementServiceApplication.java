package pe.com.nttdata.movement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MovementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovementServiceApplication.class, args);
	}

}
