package com.ab.eduplatform.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class ProgressFilter {

    String firstname;
    String lastname;
}
