package pe.com.nttdata.movement.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pe.com.nttdata.movement.event.Event;
import pe.com.nttdata.movement.event.EventType;
import pe.com.nttdata.movement.event.MovementCreatedEvent;
import pe.com.nttdata.movement.model.document.Movement;

import java.util.Date;
import java.util.UUID;

@Service
public class MovementEventService {

    @Autowired
    private KafkaTemplate<String, Event<?>> producer;

    @Value("${topic.movement.name}")
    private String topicMovement;

    public void publish(Movement movement) {
        MovementCreatedEvent created = new MovementCreatedEvent();
        created.setData(movement);
        created.setId(UUID.randomUUID().toString());
        created.setType(EventType.CREATED);
        created.setDate(new Date());

        this.producer.send(topicMovement, created);
    }

}
