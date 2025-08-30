package co.com.pragma.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {

    @Id
    @Column("user_id")
    private String idUser;

    @Column("document_number")
    private String documentNumber;

    @Column("first_name")
    private String name;

    @Column("last_name")
    private String lastNames;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("address")
    private String address;

    @Column("phone_number")
    private String phoneNumber;

    @Column("email")
    private String email;

    @Column("base_salary")
    private BigDecimal baseSalary;
}
