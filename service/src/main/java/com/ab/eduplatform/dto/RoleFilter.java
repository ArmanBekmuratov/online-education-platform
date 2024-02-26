package com.ab.eduplatform.dto;

import com.ab.eduplatform.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class RoleFilter {

    String firstname;
    String lastname;
    Role role;
    Instant registrationDate;
}
