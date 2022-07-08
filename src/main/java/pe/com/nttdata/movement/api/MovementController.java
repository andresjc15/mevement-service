package pe.com.nttdata.movement.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import pe.com.nttdata.movement.model.document.Movement;
import pe.com.nttdata.movement.model.service.MovementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/movements")
@AllArgsConstructor
public class MovementController {

    private static final Logger log = LoggerFactory.getLogger(MovementController.class);

    private final MovementService movementService;

    @GetMapping
    @Operation(summary = "Get all movements")
    public Flux<Movement> getMovements() { return movementService.getAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get movement by id")
    public Mono<Movement> getMovement(@PathVariable Long id) { return movementService.findById(id); }

    @PostMapping("/saving-account/{accountId}")
    @Operation(summary = "Register movement")
    public Mono<ResponseEntity<Map<String, Object>>> registerPerSavingAccount(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<String, Object>();
        return movementService.save(movement, accountId)
                .map(mov -> {
                    log.info("[is null]: " + mov.getAddress());
                    if (mov.getAddress() == null) {
                        response.put("message", "Insufficient amount");
                    } else {
                        response.put("movement", movement);
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                });
    }

    @PostMapping("/current-account/{accountId}")
    @Operation(summary = "Register movement in current account")
    public Mono<ResponseEntity<Map<String, Object>>> registerPerCurrentAccount(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<String, Object>();
        return movementService.save(movement, accountId)
                .map(mov -> {
                    log.info("[is null]: " + mov.getAddress());
                    if (mov.getAddress() == null) {
                        response.put("message", "Insufficient amount");
                    } else {
                        response.put("movement", movement);
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                });
    }

    @PostMapping("/fixed-term/{accountId}")
    @Operation(summary = "Register movement in fixed term")
    public Mono<ResponseEntity<Map<String, Object>>> registerPerFixedTerm(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<String, Object>();
        return movementService.save(movement, accountId)
                .map(mov -> {
                    log.info("[is null]: " + mov.getAddress());
                    if (mov.getAddress() == null) {
                        response.put("message", "Insufficient amount");
                    } else {
                        response.put("movement", movement);
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                });
    }

    @PostMapping("/personal-credit/{accountId}")
    @Operation(summary = "Register movement in personal credit")
    public Mono<ResponseEntity<Map<String, Object>>> registerPerPersonalCredit(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<String, Object>();
        return movementService.save(movement, accountId)
                .map(mov -> {
                    log.info("[is null]: " + mov.getAddress());
                    if (mov.getAddress() == null) {
                        response.put("message", "Insufficient amount");
                    } else {
                        response.put("movement", movement);
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                });
    }

    @PostMapping("/business-credit/{accountId}")
    @Operation(summary = "Register movement in business credit")
    public Mono<ResponseEntity<Map<String, Object>>> registerPerBusinessCredit(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<String, Object>();
        return movementService.save(movement, accountId)
                .map(mov -> {
                    log.info("[is null]: " + mov.getAddress());
                    if (mov.getAddress() == null) {
                        response.put("message", "Insufficient amount");
                        return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(response);
                    }
                        response.put("movement", movement);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                });
    }

    @PostMapping("/credit-card/{accountId}")
    @Operation(summary = "Register movement in credit card")
    public Mono<ResponseEntity<Map<String, Object>>> registerPerCreditCard(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        Map<String, Object> response = new HashMap<String, Object>();
        return movementService.save(movement, accountId)
                .map(mov -> {
                    log.info("[is null]: " + mov.getAddress());
                    if (mov.getAddress() == null) {
                        response.put("message", "Insufficient amount");
                    } else {
                        response.put("movement", movement);
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel movement")
    public Mono<Movement> cancelMovement(@PathVariable Long id, @RequestBody Movement movement) {
        return movementService.delete(id, movement.getDescription());
    }

    @GetMapping("/page/{page}/term/{term}")
    @Operation(summary = "Get all movements pagination")
    public Flux<Movement> getMovementsPagination(@PathVariable String term, @PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 3);
        term = "";
        return movementService.getAllPagination(term, pageable);
    }

    @GetMapping("/count")
    @Operation(summary = "Get quantity of movements")
    public Mono<Long> getQuantityMovements() {
        return movementService.getQuantityMovements();
    }

    @GetMapping("/dates/{startDate}/{endDate}")
    @Operation(summary = "Get movement between dates")
    public Flux<Movement> getByDates(@PathVariable Date startDate, @PathVariable Date endDate) {
        return movementService.getAllBetweenDates(startDate, endDate);
    }

    @GetMapping("/dates/{startDate}/{endDate}/quantity")
    @Operation(summary = "Get movement between dates")
    public Mono<Long> getQuantityByDates(@PathVariable Date startDate, @PathVariable Date endDate) {
        return movementService.getQuantityBetweenDates(startDate, endDate);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get movement by date")
    public Flux<Movement> getByDate(@PathVariable Date date) {
        return movementService.getAllByDate(date);
    }

    @GetMapping("/date/{date}/quantity")
    @Operation(summary = "Get movement by date")
    public Flux<Movement> getQuantityByDate(@PathVariable Date date) {
        return movementService.getAllByDate(date);
    }

}
