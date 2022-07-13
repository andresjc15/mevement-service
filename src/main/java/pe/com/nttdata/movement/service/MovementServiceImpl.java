package pe.com.nttdata.movement.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pe.com.nttdata.movement.client.account.model.service.AccountService;
import pe.com.nttdata.movement.model.document.Movement;
import pe.com.nttdata.movement.model.repository.MovementRepository;
import pe.com.nttdata.movement.model.service.MovementService;
import pe.com.nttdata.movement.util.SequenceGeneratorService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final SequenceGeneratorService sequenceGeneratorService;

    private final MovementRepository movementRepository;

    private final AccountService accountService;

    private final MovementEventService movementEventService;

    private ReactiveMongoOperations mongoOperations;

    @Override
    public Flux<Movement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    public Flux<Movement> getAllPagination(String term, Pageable pageable) {
        return movementRepository.findByAddressContains(term, pageable);
    }

    @Override
    public Flux<Movement> getAllBetweenDates(Date startDate, Date endDate) {
        Query query = getQueryByDates(startDate, endDate);
        return mongoOperations.find(query, Movement.class);
    }

    @Override
    public Mono<Long> getQuantityBetweenDates(Date startDate, Date endDate) {
        Query query = getQueryByDates(startDate, endDate);
        return mongoOperations.count(query, Movement.class);
    }

    private Query getQueryByDates(Date startDate, Date endDate) {
        LocalDateTime startLocalDate = startDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime endLocalDate = endDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        log.info("[startDate]: " + startDate);
        log.info("[endDate]: " + endDate);
        Criteria publishedDateCriteria = Criteria
                .where("time").gte(startLocalDate)
                .lte(endLocalDate);
        Query query = new Query(publishedDateCriteria);
        return query;
    }

    @Override
    public Flux<Movement> getAllByDate(Date date) {
        Query query = getQueryByDate(date);
        return mongoOperations.find(query, Movement.class);
    }

    @Override
    public Mono<Long> getQuantityByDate(Date date) {
        Query query = getQueryByDate(date);
        return mongoOperations.count(query, Movement.class);
    }

    private Query getQueryByDate(Date date) {
        LocalDateTime localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime startDate = LocalDateTime.
                of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0 , 0);
        LocalDateTime endDate = LocalDateTime.
                of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 23 , 59);
        log.info("[startDate]: " + startDate);
        log.info("[endDate]: " + endDate);
        Criteria publishedDateCriteria = Criteria
                .where("time")
                .gte(startDate)
                .lte(endDate);
        Query query = new Query(publishedDateCriteria);
        return query;
    }

    @Override
    public Mono<Long> getQuantityMovements() {
        return movementRepository.count();
    }

    @Override
    public Mono<Movement> save(Movement movement, Long accountId) throws ExecutionException, InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setCanceled(false);
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                log.info("[SAVING OBJECT]: " + movement);
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return Mono.just(new Movement());
        });
    }

    @Override
    public Mono<Movement> update(Movement movement) {
        return null;
    }

    @Override
    public Mono<Movement> delete(Long id, String description) {
        return movementRepository.findById(id).flatMap(m -> {
            m.setDescription(description);
            m.setCanceled(true);
            m.setUpdatedAt(new Date());
            return movementRepository.save(m);
        });
    }

    @Override
    public Mono<Movement> findById(Long id) {
        return movementRepository.findById(id);
    }

    @Override
    public Mono<Boolean> existById(Long id) {
        return movementRepository.existsById(id);
    }

    @Override
    public Mono<Movement> saveSavingAccount(Movement movement, Long accountId) throws ExecutionException,
            InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setActive(true);
                movement.setCanceled(false);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                accountService.update(acc).subscribe();
                this.movementEventService.publish(movement);
                return movementRepository.save(movement).doOnSuccess(obj ->
                        log.info("[MOVEMENT SAVED SUCCESSFULLY]: " + obj)
                );
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }

    @Override
    public Mono<Movement> saveCurrentAccount(Movement movement, Long accountId) throws ExecutionException,
            InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                accountService.update(acc).subscribe();
                return movementRepository.save(movement).doOnSuccess(obj ->
                        log.info("[MOVEMENT SAVED SUCCESSFULLY]: " + obj)
                );
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }

    @Override
    public Mono<Movement> saveFixedTerm(Movement movement, Long accountId) throws ExecutionException,
            InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                accountService.update(acc).subscribe();
                return movementRepository.save(movement).doOnSuccess(obj ->
                        log.info("[MOVEMENT SAVED SUCCESSFULLY]: " + obj)
                );
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }

    @Override
    public Mono<Movement> savePersonalCredit(Movement movement, Long accountId) throws ExecutionException,
            InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                accountService.update(acc).subscribe();
                return movementRepository.save(movement).doOnSuccess(obj ->
                        log.info("[MOVEMENT SAVED SUCCESSFULLY]: " + obj)
                );
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }

    @Override
    public Mono<Movement> saveBusinessCredit(Movement movement, Long accountId) throws ExecutionException,
            InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                accountService.update(acc).subscribe();
                return movementRepository.save(movement).doOnSuccess(obj ->
                        log.info("[MOVEMENT SAVED SUCCESSFULLY]: " + obj)
                );
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }

    @Override
    public Mono<Movement> saveCreditCard(Movement movement, Long accountId) throws ExecutionException,
            InterruptedException {
        movement.setId(sequenceGeneratorService.generateSequence(Movement.SEQUENCE_NAME));
        log.info("[SAVING]");
        return accountService.findById(accountId).flatMap(acc -> {
            BigDecimal newAmount = acc.getAmount().add(movement.getAmount());
            log.info("[New Amount]: " + newAmount);
            if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.info("[Movement Amount]: " + movement.getAmount());
                log.info("[Account Amount]: " + acc.getAmount());
                movement.setBeforeAmount(acc.getAmount());
                movement.setAfterAmount(newAmount);
                movement.setTime(LocalDateTime.now());
                movement.setAccount(acc);
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                accountService.update(acc).subscribe();
                return movementRepository.save(movement).doOnSuccess(obj ->
                        log.info("[MOVEMENT SAVED SUCCESSFULLY]: " + obj)
                );
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }
}
