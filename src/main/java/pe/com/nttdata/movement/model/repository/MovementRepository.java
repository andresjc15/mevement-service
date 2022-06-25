package pe.com.nttdata.movement.model.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.com.nttdata.movement.model.document.Movement;

@Repository
public interface MovementRepository extends ReactiveMongoRepository<Movement, Long> {
}
