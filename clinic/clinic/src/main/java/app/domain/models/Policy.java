package app.domain.models;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor

public class Policy {
    private long id;
    private Company company;
    private String policyNumber;
    private boolean active;
    private Date expiryDate;
}
