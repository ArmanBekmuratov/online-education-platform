package com.ab.eduplatform.http.controller;

import com.ab.eduplatform.dto.PageResponse;
import com.ab.eduplatform.dto.course.CourseCreateEditDto;
import com.ab.eduplatform.dto.course.CourseFilter;
import com.ab.eduplatform.dto.course.CourseReadDto;
import com.ab.eduplatform.dto.user.UserCreateEditDto;
import com.ab.eduplatform.dto.user.UserFilter;
import com.ab.eduplatform.dto.user.UserReadDto;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String findAll(Model model, CourseFilter filter, Pageable pageable) {
        Page<CourseReadDto> page = courseService.findAll(filter, pageable);
        model.addAttribute("courses", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "/course/courses";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return courseService.findById(id)
                .map(course -> {
                    model.addAttribute("course", course);
                    return "course/courses";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public String create(@ModelAttribute CourseCreateEditDto course) {
        return "redirect:/courses/" + courseService.create(course).getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute CourseCreateEditDto course) {
        return courseService.update(id, course)
                .map(it -> "redirect:/course/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!courseService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/courses";
    }
}
