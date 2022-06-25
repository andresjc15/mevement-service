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

    @PostMapping("/saving-account/{id}")
    public Mono<Movement> registerPerSavingAccount(@PathVariable Long id, @RequestBody Movement movement) {
        return null;
    }

    @PostMapping("/current-account/{id}")
    public Mono<Movement> registerPerCurrentAccount(@PathVariable Long id, @RequestBody Movement movement) {
        return null;
    }

    @PostMapping("/fixed-term/{id}")
    public Mono<Movement> registerPerFixedTerm(@PathVariable Long id, @RequestBody Movement movement) {
        return null;
    }

    @PostMapping("/personal-credit/{id}")
    public Mono<Movement> registerPerPersonalCredit(@PathVariable Long id, @RequestBody Movement movement) {
        return null;
    }

    @PostMapping("/business-credit/{id}")
    public Mono<Movement> registerPerBusinessCredit(@PathVariable Long id, @RequestBody Movement movement) {
        return null;
    }

    @PostMapping("/credit-card/{id}")
    public Mono<Movement> registerPerCreditCard(@PathVariable Long id, @RequestBody Movement movement) {
        return null;
    }

    @DeleteMapping("/{id}")
    public Mono<Movement> cancelMovement(@PathVariable Long id) {
        return null;
    }

}
