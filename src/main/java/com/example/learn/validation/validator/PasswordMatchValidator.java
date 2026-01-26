package com.example.learn.validation.validator;

import com.example.learn.auth.dto.RegisterRequest;
import com.example.learn.validation.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(registerRequest.getPassword() == null || registerRequest.getConfirmPassword() == null) {
            return false;
        }
        return registerRequest.getPassword().equals(registerRequest.getConfirmPassword());
    }
}
