package com.evaluacion.users_api.mapper;

import com.evaluacion.users_api.dto.UserResponse;
import com.evaluacion.users_api.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .modified(user.getModified())
                .last_login(user.getLastLogin())
                .token(user.getToken())
                .isactive(user.getIsActive())
                .build();
    }
}