package com.evaluacion.users_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import com.evaluacion.users_api.validation.PasswordRegex;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
            message = "Formato de correo inválido"
    )
    private String email;

    @NotBlank
    @PasswordRegex
    private String password;

    @Valid
    private List<PhoneRequest> phones;
}