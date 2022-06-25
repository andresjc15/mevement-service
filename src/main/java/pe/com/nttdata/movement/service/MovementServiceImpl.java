package pe.com.nttdata.movement.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.com.nttdata.movement.model.document.Movement;
import pe.com.nttdata.movement.model.repository.MovementRepository;
import pe.com.nttdata.movement.model.service.MovementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class MovementServiceImpl implements MovementService {

    private static final Logger log = LoggerFactory.getLogger(MovementServiceImpl.class);

    private final MovementRepository movementRepository;
    @Override
    public Flux<Movement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    public Mono<Movement> save(Movement movement) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Mono<Movement> update(Movement movement) {
        return null;
    }

    @Override
    public Mono<Movement> delete(Long id) {
        return null;
    }

    @Override
    public Mono<Movement> findById(Long id) {
        return null;
    }

    @Override
    public Mono<Boolean> existById(Long id) {
        return null;
    }
}
