package pe.com.nttdata.movement.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.com.nttdata.movement.client.account.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movements")
public class Movement {

    @Transient
    public static final String SEQUENCE_NAME = "movements_sequence";

    private Long id;
    private Long numberMovement;
    private BigDecimal amount;
    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;
    private String address;
    private LocalDateTime time;
    private String description;
    private Account account;
    private boolean canceled;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

}
