package pe.com.nttdata.movement.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.com.nttdata.movement.client.account.model.Account;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @Id
    private Long id;

    @NotNull
    @Min(value = 999999999999L)
    @Max(value = 9999999999999L)
    private Long numberMovement;

    @NotNull
    private BigDecimal amount;

    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;

    @NotBlank(message = "address can't be blank")
    private String address;
    private LocalDateTime time;
    private String description;
    private Account account;
    private boolean canceled;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

}
