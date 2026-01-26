package com.example.learn.auth.dto;

import com.example.learn.user.UserType;
import com.example.learn.validation.annotation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PasswordMatch
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email
    private String email;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank
    @Size(min = 5, message = "Password should have length of 5")
    private String password;
    @NotBlank()
    private String confirmPassword;
    @NotNull()
    private UserType userType;
}
