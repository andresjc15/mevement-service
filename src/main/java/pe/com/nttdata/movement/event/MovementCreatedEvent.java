package pe.com.nttdata.movement.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.com.nttdata.movement.model.document.Movement;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovementCreatedEvent extends Event<Movement> {
}
