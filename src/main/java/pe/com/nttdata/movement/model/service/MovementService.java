package pe.com.nttdata.movement.model.service;

import pe.com.nttdata.movement.model.document.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface MovementService {

    Flux<Movement> getAll();
    Mono<Movement> save(Movement movement) throws ExecutionException, InterruptedException;
    Mono<Movement> update(Movement movement);
    Mono<Movement> delete(Long id);
    Mono<Movement> findById(Long id);
    Mono<Boolean> existById(Long id);

}
