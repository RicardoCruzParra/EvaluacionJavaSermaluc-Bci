package com.evaluacion.users_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordRegexValidator
        implements ConstraintValidator<PasswordRegex, String> {

    @Value("${security.password-regex}")
    private String regex;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(regex);
    }
}