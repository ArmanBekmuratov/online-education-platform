package com.ab.eduplatform.mapper;

import com.ab.eduplatform.dto.user.UserReadDto;
import com.ab.eduplatform.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return new UserReadDto(
                object.getId(),
                object.getEmail(),
                object.getFirstname(),
                object.getLastname(),
                object.getRole(),
                object.getRegistrationDate()
        );
    }
}