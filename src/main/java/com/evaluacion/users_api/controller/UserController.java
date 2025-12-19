package com.evaluacion.users_api.controller;

import com.evaluacion.users_api.dto.UserRequest;
import com.evaluacion.users_api.dto.UserResponse;
import com.evaluacion.users_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRequest request) {

        String requestId = UUID.randomUUID().toString();

        log.info("[{}] POST /api/users - request: name='{}', email='{}', phones={}",
                requestId,
                request.getName(),
                request.getEmail(),
                request.getPhones() == null ? 0 : request.getPhones().size()
        );

        UserResponse response = userService.register(requestId, request);

        log.info("[{}] POST /api/users - response: id={}, isactive={}, created={}, last_login={}",
                requestId,
                response.getId(),
                response.getIsactive(),
                response.getCreated(),
                response.getLast_login()
        );

        return response;
    }
}
