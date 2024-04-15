package com.ab.eduplatform.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class LoginDto {

    @Email
    String email;

    @NotBlank
    String password;
}
