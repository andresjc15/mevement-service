package pe.com.nttdata.movement.model.service;

import org.springframework.data.domain.Pageable;
import pe.com.nttdata.movement.model.document.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public interface MovementService {

    Flux<Movement> getAll();

    Flux<Movement> getAllPagination(String term, Pageable pageable);

    Flux<Movement> getAllBetweenDates(Date startDate, Date endDate);

    Mono<Long> getQuantityBetweenDates(Date startDate, Date endDate);

    Flux<Movement> getAllByDate(Date date);

    Mono<Long> getQuantityByDate(Date date);

    Mono<Long> getQuantityMovements();
    Mono<Movement> save(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;
    Mono<Movement> update(Movement movement);
    Mono<Movement> delete(Long id, String description);
    Mono<Movement> findById(Long id);
    Mono<Boolean> existById(Long id);

    Mono<Movement> saveSavingAccount(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;
    Mono<Movement> saveCurrentAccount(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;
    Mono<Movement> saveFixedTerm(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;
    Mono<Movement> savePersonalCredit(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;
    Mono<Movement> saveBusinessCredit(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;
    Mono<Movement> saveCreditCard(Movement movement, Long accountId)
            throws ExecutionException, InterruptedException;

}
