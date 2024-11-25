package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {

    @NotEmpty(message = "Username is Required")
    String username;
    @NotEmpty(message = "Password is Required")
    String password;
}
