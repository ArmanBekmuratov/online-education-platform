package com.ab.eduplatform.dto.course;

import com.ab.eduplatform.entity.Level;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;

@Value
public class CourseReadDto {

    @NotNull
    Long id;

    @NotNull
    String name;

    @NotNull
    String description;

    @NotNull
    Double price;

    @NotNull
    Level level;

    @NotNull
    String category;

    @NotNull
    String teacherName;

    @NotNull
    LocalDate startDate;

    @NotNull
    LocalDate endDate;

    String image;
}
