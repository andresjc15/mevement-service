package pe.com.nttdata.movement;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.nttdata.movement.client.account.model.Account;
import pe.com.nttdata.movement.model.document.Movement;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovementServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	private final String accountUri = "http://localhost:20200/api/accounts";

	private final String movementUri = "http://localhost:20300/api/movements";

	@Test
	@Order(1)
	public void testCreateAccount() {
		Account request = new Account();
		request.setAmount(new BigDecimal(1000.00));
		request.setCustomerId(4L);

		webTestClient.post()
				.uri(accountUri + "/saving-account")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), Account.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.amount").isNotEmpty()
				.jsonPath("$.amount").isEqualTo(new BigDecimal(1000.00));
	}

	@Test
	@Order(2)
	public void testGetAllAccounts() {
		webTestClient.get()
				.uri(accountUri)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Movement.class);
	}

	@Test
	@Order(3)
	public void testGetSingleAccount() {
		webTestClient.get()
				.uri(accountUri+"/{id}", 13)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response ->
						Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	@Order(4)
	public void testGetAllMovements() {
		webTestClient.get()
				.uri(movementUri)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Movement.class);
	}

	@Test
	@Order(5)
	public void testGetSingleMovement() {
		webTestClient.get()
				.uri(movementUri + "/{id}", 123)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response ->
						Assertions.assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	@Order(6)
	public void testDeleteMovement() {
		Movement request = new Movement();
		request.setDescription("fraudulent");
		webTestClient.method(HttpMethod.DELETE)
				.uri(movementUri + "/{id}", 121)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), Movement.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.canceled").isNotEmpty()
				.jsonPath("$.canceled").isEqualTo(true);
	}

}
