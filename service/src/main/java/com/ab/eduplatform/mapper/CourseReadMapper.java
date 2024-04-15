package com.ab.eduplatform.mapper;

import com.ab.eduplatform.dto.course.CourseReadDto;
import com.ab.eduplatform.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseReadMapper implements Mapper<Course, CourseReadDto> {

    @Override
    public CourseReadDto map(Course object) {
        return new CourseReadDto(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getPrice(),
                object.getLevel(),
                object.getCategory(),
                object.getTeacher().getFirstname().concat(object.getTeacher().getLastname()),
                object.getStartDate(),
                object.getEndDate(),
                object.getImage()
        );
    }
}
