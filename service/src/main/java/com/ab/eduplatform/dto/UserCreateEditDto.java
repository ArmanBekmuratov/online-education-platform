package com.ab.eduplatform.dto;

import com.ab.eduplatform.entity.Role;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Value
@FieldNameConstants
public class UserCreateEditDto {

    String email;
    String firstname;
    String lastname;
    Role role;
    Instant registrationDate;
}