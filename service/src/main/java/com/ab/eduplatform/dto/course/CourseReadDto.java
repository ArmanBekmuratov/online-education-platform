package com.ab.eduplatform.dto.course;

import com.ab.eduplatform.entity.Level;
import lombok.Value;

import java.time.LocalDate;

@Value
public class CourseReadDto {

    Long id;
    String name;
    String description;
    Double price;
    Level level;
    String category;
    String teacherName;
    LocalDate startDate;
    LocalDate endDate;
    String image;
}
