package com.ab.eduplatform.dto.user;

import com.ab.eduplatform.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    @Email
    String email;

    @NotNull
    @Size(min = 2, max = 64)
    String firstname;

    @NotNull
    @Size(min = 2, max = 64)
    String lastname;

    @NotNull
    Role role;

    @NotNull
    String password;

    Instant registrationDate;
}