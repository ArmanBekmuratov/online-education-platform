package com.ab.eduplatform.dto;

import com.ab.eduplatform.entity.Role;
import lombok.Value;

import java.time.Instant;

@Value
public class UserReadDto {

    Long id;
    String email;
    String firstname;
    String lastname;
    Role role;
    Instant registrationDate;
}
