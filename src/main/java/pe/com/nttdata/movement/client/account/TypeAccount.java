package pe.com.nttdata.movement.client.account;

import lombok.Data;

import java.util.Date;

@Data
public class TypeAccount {

    private Long id;
    private String name;
    private String sub;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

}
