package pe.com.nttdata.movement.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movements")
public class Movement {

    private Long id;
    private Long numberMovement;
    private String description;

}
