package pe.com.nttdata.movement.client.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.nttdata.movement.client.account.TypeAccount;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private long id;
    private String hexId;
    private Long customerId;
    private TypeAccount typeAccount;
    private Long numberAccount;
    private BigDecimal amount;
    private List<Map<String, Object>> transactions;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

}
