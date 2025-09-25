package co.com.pragma.crediya.api.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {


    private String id;


    @NotNull(message = "Document number is required")
    @NotBlank(message = "Document number cannot be blank")
    private String documentNumber;


    @NotNull(message = "First name is required")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;


    @NotNull(message = "Last name is required")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;


    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;


    @NotNull(message = "Address is required")
    @NotBlank(message = "Address cannot be blank")
    private String address;


    @NotNull(message = "Phone number is required")
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phoneNumber;


    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;


    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.00", message = "Base salary must be greater than or equal to 0.00")
    @DecimalMax(value = "15000000.00", message = "Base salary must not exceed 15,000,000.00")
    @Digits(integer = 15, fraction = 2, message = "Invalid salary format (max. 2 decimals)")
    private BigDecimal baseSalary;
}

