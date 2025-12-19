package com.evaluacion.users_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordRegexValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordRegex {
    String message() default "Formato de contraseña inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}