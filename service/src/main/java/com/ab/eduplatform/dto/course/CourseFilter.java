package com.ab.eduplatform.dto.course;

import com.ab.eduplatform.entity.Level;

public record CourseFilter(String name,
                           String description,
                           Level level,
                           String category,
                           Double minPrice,
                           Double maxPrice ) {
}
