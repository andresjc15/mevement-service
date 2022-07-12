package pe.com.nttdata.movement;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.nttdata.movement.client.account.TypeAccount;
import pe.com.nttdata.movement.client.account.model.Account;
import pe.com.nttdata.movement.model.document.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovementServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	private final String accountUri = "http://localhost:20200/api/accounts";

	private final String movementUri = "http://localhost:20300/api/movements";

	private Account account;
	private TestInfo testInfo;
	private TestReporter testReporter;

	@BeforeEach
	void initMethodTest(TestInfo testInfo, TestReporter testReporter) {
		this.account = new Account(0001L,"00000020f51bb4362eee2a4d", 5419857469152L, new TypeAccount(),
				1648517941534L, new BigDecimal(300.00), null, true,new Date(), null);
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		log.info("Iniciando el metodo test");
		testReporter.publishEntry(" ejecutando: " + testInfo.getDisplayName() + " "
				+ testInfo.getTestMethod().orElse(null).getName()
				+ " con las etiquetas " + testInfo.getTags());
	}

	@Test
	@DisplayName("Error en caso de prueba numero de cuenta")
	@Order(1)
	void testNumberAccount() {
		assertNotNull(account.getNumberAccount());
		assertEquals(1648517941534L, account.getNumberAccount().longValue());
		assertFalse(account.getNumberAccount().compareTo(0L) < 0);
		assertTrue(account.getNumberAccount().compareTo(0L) > 0);
	}

	@Test
	@Order(2)
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
	@Order(3)
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
	@Order(4)
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
	@Order(5)
	public void testGetAllMovements() {
		FluxExchangeResult<Movement> MovementsResult = webTestClient.get()
				.uri(movementUri)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.returnResult(Movement.class);

		EntityExchangeResult<Movement> movementResult = webTestClient.get()
				.uri(movementUri + "/{id}", 120)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Movement.class)
				.consumeWith(response ->
						Assertions.assertThat(response.getResponseBody()).isNotNull())
				.returnResult();

		Flux<Movement> movements = MovementsResult.getResponseBody();
		Movement movement = movementResult.getResponseBody();

		StepVerifier.create(movements)
				.expectNext(movement)
				.expectNextCount(27)
				.verifyComplete();
	}

	@Test
	@Order(6)
	public void testGetSingleMovement() {
		EntityExchangeResult<Movement> movement = webTestClient.get()
				.uri(movementUri + "/{id}", 123)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Movement.class)
				.consumeWith(response ->
						Assertions.assertThat(response.getResponseBody()).isNotNull())
				.returnResult();
	}

	@Test
	@Order(7)
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

	@Test
	@Order(8)
	public void testGetQuantity() {
		webTestClient.method(HttpMethod.GET)
				.uri(movementUri + "/count")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$").isNotEmpty();
				//.jsonPath("$").isEqualTo(22L);
	}

	@Test
	@Order(9)
	public void testGetQuantityByDate() {
		String date = "12/07/2022";
		webTestClient.method(HttpMethod.GET)
				.uri(movementUri + "/date/{date}/quantity", date)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	@Test
	@Order(10)
	public void testGetAllMovementsByDates() {
		String startDate = "11/07/2022";
		String endDate = "12/07/2022";
		FluxExchangeResult<Movement> MovementsResult = webTestClient.get()
				.uri(movementUri + "/dates/{startDate}/{endDate}", startDate, endDate)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.returnResult(Movement.class);
	}

	@Test
	@Order(11)
	public void testGetQuantityByDateS() {
		String startDate = "11/07/2022";
		String endDate = "12/07/2022";
		webTestClient.method(HttpMethod.GET)
				.uri(movementUri + "/dates/{startDate}/{endDate}/quantity", startDate, endDate)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	@Test
	@Order(12)
	public void testCreateMovement() {
		Movement request = new Movement();
		request.setDescription("purchase online");
		request.setAmount(new BigDecimal(-29.90));
		String accountId = "13";

		webTestClient.post()
				.uri(movementUri + "/saving-account/{accountId}", accountId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), Movement.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

}
