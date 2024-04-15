package com.ab.eduplatform.dto.course;

import com.ab.eduplatform.entity.Level;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Value
public class CourseCreateEditDto {

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

    Long teacherId;

    @NotNull
    LocalDate startDate;

    @NotNull
    LocalDate endDate;

    MultipartFile image;
}
