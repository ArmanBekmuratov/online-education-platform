package com.ab.eduplatform.service;

import com.ab.eduplatform.dto.user.UserCreateEditDto;
import com.ab.eduplatform.dto.user.UserFilter;
import com.ab.eduplatform.dto.user.UserReadDto;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.mapper.UserCreateEditMapper;
import com.ab.eduplatform.mapper.UserReadMapper;
import com.ab.eduplatform.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceIT {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserReadMapper userReadMapper;

    @Mock
    private UserCreateEditMapper userCreateEditMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllPageable() {
        Page<User> users = new PageImpl<>(List.of(getUser()));
        UserReadDto userReadDto = getUserReadDto();
        when(userRepository.findAll((Predicate) any(), any(Pageable.class))).thenReturn(users);
        when(userReadMapper.map(any(User.class))).thenReturn(userReadDto);

        Page<UserReadDto> result = userService.findAll(new UserFilter("ivan", "ivanko"), PageRequest.of(0, 10));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(userReadDto);
    }

    @Test
    void findById() {
        User user = getUser();
        UserReadDto userReadDto = getUserReadDto();
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userReadMapper.map(user)).thenReturn(userReadDto);

        Optional<UserReadDto> result = userService.findById(userId);

        assertThat(result).isPresent().contains(userReadDto);
    }

    @Test
    void shouldCreateUser() {
        User user = getUser();
        UserCreateEditDto userDto = getUserCreateEditDto();
        UserReadDto userReadDto = getUserReadDto();
        when(userCreateEditMapper.map(userDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userReadMapper.map(user)).thenReturn(userReadDto);

        UserReadDto result = userService.create(userDto);

        assertThat(result).isEqualTo(userReadDto);
    }

    @Test
    void shouldUpdateUser() {
        Long userId = 1L;
        User existingUser = getUser();
        UserCreateEditDto userDto = getUserCreateEditDto();
        User updatedUser = getUser();
        updatedUser.setFirstname("updatedName");
        UserReadDto userReadDto = getUserReadDto();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userCreateEditMapper.map(userDto, existingUser)).thenReturn(updatedUser);
        when(userRepository.saveAndFlush(updatedUser)).thenReturn(updatedUser);
        when(userReadMapper.map(updatedUser)).thenReturn(userReadDto);

        Optional<UserReadDto> result = userService.update(userId, userDto);

        assertThat(result).isPresent().contains(userReadDto);
    }

    @Test
    void shouldDeleteWhenUserExists() {
        User user = getUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        boolean result = userService.delete(user.getId());

        assertThat(result).isTrue();
    }

    @Test
    void shouldNotDeleteWhenUserDoesNotExist() {
        Long userId = 100000000L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        boolean result = userService.delete(userId);

        assertThat(result).isFalse();
    }

    private UserCreateEditDto getUserCreateEditDto() {
        return new UserCreateEditDto("ivan@gmail.com", "ivan", "ivanko", Role.ADMIN, "123" ,Instant.now());
    }

    private UserReadDto getUserReadDto() {
        return new UserReadDto(1L, "ivan@gmail.com", "ivan", "ivanko", Role.ADMIN, Instant.now());
    }

    private User getUser() {
        return User.builder()
                .firstname("ivan")
                .lastname("ivanko")
                .email("ivan@gmail.com")
                .password("123")
                .role(Role.ADMIN)
                .registrationDate(Instant.now())
                .build();
    }
}
