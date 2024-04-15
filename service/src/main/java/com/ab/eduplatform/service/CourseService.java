package com.ab.eduplatform.service;

import com.ab.eduplatform.dto.CategoryFilter;
import com.ab.eduplatform.dto.course.CourseCreateEditDto;
import com.ab.eduplatform.dto.course.CourseFilter;
import com.ab.eduplatform.dto.course.CourseReadDto;
import com.ab.eduplatform.dto.user.UserFilter;
import com.ab.eduplatform.dto.user.UserReadDto;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.mapper.CourseCreateEditMapper;
import com.ab.eduplatform.mapper.CourseReadMapper;
import com.ab.eduplatform.repository.CourseRepository;
import com.ab.eduplatform.repository.QPredicate;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.ab.eduplatform.entity.QCourse.course;
import static com.ab.eduplatform.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseReadMapper courseReadMapper;
    private final CourseCreateEditMapper courseCreateEditMapper;
    private final ImageService imageService;

    public Page<CourseReadDto> findAll(CourseFilter filter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(filter.name(), course.name::containsIgnoreCase)
                .add(filter.description(), course.description::containsIgnoreCase)
                .add(filter.category(), course.category::containsIgnoreCase)
                .add(filter.level(), course.level::eq)
                .buildAnd();

        return courseRepository.findAll(predicate, pageable)
                .map(courseReadMapper::map);
    }

    public List<CourseReadDto> findAll() {
        return courseRepository.findAll().stream()
                .map(courseReadMapper::map)
                .toList();
    }

    public Optional<CourseReadDto> findById(Long id) {
        return courseRepository.findById(id)
                .map(courseReadMapper::map);
    }

    @Transactional
    public CourseReadDto create(CourseCreateEditDto courseDto) {
        return Optional.of(courseDto)
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return courseCreateEditMapper.map(dto);
                })
                .map(courseRepository::save)
                .map(courseReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<CourseReadDto> update(Long id, CourseCreateEditDto courseDto) {
        return courseRepository.findById(id)
                .map(entity -> {
                    uploadImage(courseDto.getImage());
                    return courseCreateEditMapper.map(courseDto, entity);
                })
                .map(courseRepository::saveAndFlush)
                .map(courseReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return courseRepository.findById(id)
                .map(entity -> {
                    courseRepository.delete(entity);
                    courseRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Optional<byte[]> findAvatar(Long id) {
        return courseRepository.findById(id)
                .map(Course::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }
}
