package pe.com.nttdata.movement.model.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "movements")
public class Movement {

    private Long id;
    private Long numberMovement;
    private String description;

}
