package com.ab.eduplatform.controller;

import com.ab.eduplatform.dto.user.UserReadDto;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.http.controller.UserController;
import com.ab.eduplatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    void findByIdIsFound() throws Exception {
        when(userService.findById(eq(1L))).thenReturn(Optional.of(getUserReadDto()));

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user"))
                .andExpect(model().attributeExists("user", "roles"));
    }

    @Test
    void findByIdIsNotFound() throws Exception {
        when(userService.findById(eq(1L))).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreate() throws Exception {
        UserReadDto createdUser = getUserReadDto();
        when(userService.create(any())).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .param("firstname", "John")
                        .param("lastname", "Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/1"));
    }

    @Test
    void shouldUpdateIfFound() throws Exception {
        when(userService.update(eq(1L), any())).thenReturn(Optional.of(getUserReadDto()));

        mockMvc.perform(post("/users/{id}/update", 1)
                        .param("firstname", "ivan")
                        .param("lastname", "ivanko"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/1"));
    }

    @Test
    void shouldNotUpdateIfNotFound() throws Exception {
        when(userService.update(eq(1L), any())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post("/users/{id}/update", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteIfFound() throws Exception {
        when(userService.delete(eq(1L))).thenReturn(true);

        mockMvc.perform(post("/users/{id}/delete", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void shouldNotDeleteIfNotFound() throws Exception {
        when(userService.delete(eq(1L))).thenReturn(false);

        mockMvc.perform(post("/users/{id}/delete", 1L))
                .andExpect(status().isNotFound());
    }

    private UserReadDto getUserReadDto() {
        return new UserReadDto(1L, "ivan@gmail.com", "ivan", "ivanko", Role.ADMIN, Instant.now());
    }

}
