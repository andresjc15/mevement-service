package pe.com.nttdata.movement.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class MovementServiceImpl implements MovementService {

    private static final Logger log = LoggerFactory.getLogger(MovementServiceImpl.class);

    private final SequenceGeneratorService sequenceGeneratorService;

    private final MovementRepository movementRepository;

    private final AccountService accountService;

    @Override
    public Flux<Movement> getAll() {
        return movementRepository.findAll();
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
                movement.setActive(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }

    @Override
    public Mono<Movement> update(Movement movement) {
        return null;
    }

    @Override
    public Mono<Movement> delete(Long id) {
        return movementRepository.findById(id).flatMap(m -> {
            m.setCanceled(true);
            m.setUpdatedAt(new Date());
            return movementRepository.save(m);
        });
    }

    @Override
    public Mono<Movement> findById(Long id) {
        return null;
    }

    @Override
    public Mono<Boolean> existById(Long id) {
        return null;
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
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(null);
                acc.setAmount(newAmount);
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
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
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
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
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
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
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
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
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
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
                log.info("[SAVING OBJECT]: " + movement.toString());
                accountService.update(acc).subscribe();
                return movementRepository.save(movement);
            }
            log.info("[INSUFFICIENT AMOUNT]");
            return null;
        });
    }
}
