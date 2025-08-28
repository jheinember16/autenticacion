package co.com.pragma.crediya.model.user;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String idUser;
    private String documentNumber;
    private String name;
    private String lastNames;
    private LocalDate birthDate;
    private String email;
    private BigDecimal baseSalary;
    private String address;
    private String phoneNumber;

}