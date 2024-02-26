package com.ab.eduplatform.dto;

import com.ab.eduplatform.entity.Level;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryFilter {

    String category;
    Level level;
}
