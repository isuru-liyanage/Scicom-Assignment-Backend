package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignupDTO {
    @NotEmpty(message = "Display name is Required")
    String displayName;
    @NotEmpty(message = "Email is Required")
    String email;
    @NotEmpty(message = "Password is Required")
    String password;
    @NotEmpty(message = "Username is Required")
    String username;
}
