package app.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor

public class Patient extends Person {
    private boolean gender;
    private String EmergencyContact;
    private Policy policy;

    
}
