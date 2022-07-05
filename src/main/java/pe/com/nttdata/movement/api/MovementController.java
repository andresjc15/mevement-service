package pe.com.nttdata.movement.api;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pe.com.nttdata.movement.model.document.Movement;
import pe.com.nttdata.movement.model.service.MovementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/movements")
@AllArgsConstructor
public class MovementController {

    private static final Logger log = LoggerFactory.getLogger(MovementController.class);

    private final MovementService movementService;

    @GetMapping
    public Flux<Movement> getMovements() { return movementService.getAll(); }

    @GetMapping("/{id}")
    public Mono<Movement> getMovement(@PathVariable Long id) { return movementService.findById(id); }

    @PostMapping("/saving-account/{accountId}")
    public Mono<Movement> registerPerSavingAccount(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        return movementService.save(movement, accountId);
    }

    @PostMapping("/current-account/{accountId}")
    public Mono<Movement> registerPerCurrentAccount(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        return movementService.save(movement, accountId);
    }

    @PostMapping("/fixed-term/{accountId}")
    public Mono<Movement> registerPerFixedTerm(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        return movementService.save(movement, accountId);
    }

    @PostMapping("/personal-credit/{accountId}")
    public Mono<Movement> registerPerPersonalCredit(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        return movementService.save(movement, accountId);
    }

    @PostMapping("/business-credit/{accountId}")
    public Mono<Movement> registerPerBusinessCredit(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        return movementService.save(movement, accountId);
    }

    @PostMapping("/credit-card/{accountId}")
    public Mono<Movement> registerPerCreditCard(@PathVariable Long accountId, @RequestBody Movement movement)
            throws ExecutionException, InterruptedException {
        return movementService.save(movement, accountId);
    }

    @DeleteMapping("/{id}")
    public Mono<Movement> cancelMovement(@PathVariable Long id, @PathVariable Long accountId) {
        return movementService.delete(id);
    }

}
