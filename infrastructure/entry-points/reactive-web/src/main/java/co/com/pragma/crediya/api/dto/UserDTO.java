package co.com.pragma.crediya.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    private String idUser;

    @NotBlank(message = "no puede estar vacío o nulo")
    private String documentNumber;

    @NotBlank(message = "no puede estar vacío o nulo")
    private String name;

    @NotBlank(message = "no puede estar vacío o nulo")
    private String lastNames;

    private LocalDate birthDate;

    @NotBlank(message = "no puede estar vacío o nulo")
    private String address;

    private String phoneNumber;

    @NotBlank(message = "no puede estar vacío o nulo")
    @Email(message = "debe tener formato válido")
    private String email;

    @DecimalMin(value = "0.00", message = "mínimo debe ser 0.0")
    @DecimalMax(value = "15000000", message = "máximo debe ser 15000000")
    @Digits(integer = 15, fraction = 0, message = "Formato de salario inválido")
    private BigDecimal baseSalary;
}