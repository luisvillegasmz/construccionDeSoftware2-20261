package app.domain.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public abstract class Person {
    private Long id;
    private String name;
    private String document;
    private String phone;
    private String email;
    private String address;
    private Date birthDate;
}
