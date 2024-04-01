package com.ab.eduplatform.mapper;

import com.ab.eduplatform.dto.UserCreateEditDto;
import com.ab.eduplatform.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);

        return user;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setEmail(object.getEmail());
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setRegistrationDate(object.getRegistrationDate());
        user.setRole(object.getRole());
    }
}