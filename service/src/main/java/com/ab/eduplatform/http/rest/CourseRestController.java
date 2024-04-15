package com.ab.eduplatform.http.rest;

import com.ab.eduplatform.dto.PageResponse;
import com.ab.eduplatform.dto.course.CourseCreateEditDto;
import com.ab.eduplatform.dto.course.CourseFilter;
import com.ab.eduplatform.dto.course.CourseReadDto;
import com.ab.eduplatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping(value = "/{id}/avatar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findAvatar(@PathVariable("id") Long id) {
        return courseService.findAvatar(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public PageResponse<CourseReadDto> findAll(CourseFilter filter, Pageable pageable) {
        Page<CourseReadDto> page = courseService.findAll(filter, pageable);

        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public CourseReadDto findById(@PathVariable("id") Long id) {
        return courseService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseReadDto create(@Validated @RequestBody CourseCreateEditDto course) {

        return courseService.create(course);
    }

    @PutMapping("/{id}/")
    public CourseReadDto update(@PathVariable("id") Long id,
                           @Validated @RequestBody CourseCreateEditDto course) {
        return courseService.update(id, course)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        if (!courseService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
