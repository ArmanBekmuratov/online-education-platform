package com.ab.eduplatform.mapper;

import com.ab.eduplatform.dto.course.CourseCreateEditDto;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;

import static java.util.function.Predicate.*;

@Component
@RequiredArgsConstructor
public class CourseCreateEditMapper implements Mapper<CourseCreateEditDto, Course> {

    private final UserRepository userRepository;

    @Override
    public Course map(CourseCreateEditDto fromObject, Course toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Course map(CourseCreateEditDto object) {
        Course course = new Course();
        copy(object, course);

        return course;
    }

    private void copy(CourseCreateEditDto object, Course course) {
        course.setName(object.getName());
        course.setDescription(object.getDescription());
        course.setPrice(object.getPrice());
        course.setLevel(object.getLevel());
        course.setCategory(object.getCategory());

        User teacher = userRepository.findById(object.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher with ID " + object.getTeacherId() + " not found"));
        course.setTeacher(teacher);
        course.setStartDate(object.getStartDate());
        course.setEndDate(object.getEndDate());
        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> course.setImage(image.getOriginalFilename()));
    }
}
