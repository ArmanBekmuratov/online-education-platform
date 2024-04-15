package com.ab.eduplatform.dto.user;

import com.ab.eduplatform.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.time.Instant;

@Value
public class UserReadDto {

    @NotBlank
    Long id;

    @Email
    String email;

    @NotBlank
    String firstname;

    @NotBlank
    String lastname;

    @NotBlank
    Role role;

    Instant registrationDate;
}
