package pe.com.nttdata.movement.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.com.nttdata.movement.model.document.Movement;
import reactor.core.publisher.Flux;

@Repository
public interface MovementRepository extends ReactiveMongoRepository<Movement, Long> {

    Flux<Movement> findByAddressContains(String term, Pageable page);

}
