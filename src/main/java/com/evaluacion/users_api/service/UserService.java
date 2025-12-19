package com.evaluacion.users_api.service;

import com.evaluacion.users_api.dto.PhoneRequest;
import com.evaluacion.users_api.dto.UserRequest;
import com.evaluacion.users_api.dto.UserResponse;
import com.evaluacion.users_api.exception.EmailAlreadyExistsException;
import com.evaluacion.users_api.mapper.UserMapper;
import com.evaluacion.users_api.model.Phone;
import com.evaluacion.users_api.model.User;
import com.evaluacion.users_api.repository.UserRepository;
import com.evaluacion.users_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(String requestId, UserRequest request) {

        log.info("[{}] Validando email único: {}", requestId, request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("[{}] Registro rechazado: email ya existe -> {}", requestId, request.getEmail());
            throw new EmailAlreadyExistsException();
        }

        log.info("[{}] Mapeando teléfonos...", requestId);
        List<Phone> phones = new ArrayList<>();
        if (request.getPhones() != null) {
            for (PhoneRequest pr : request.getPhones()) {
                if (pr == null) continue;
                phones.add(toEntity(pr));
            }
        }
        log.info("[{}] Teléfonos mapeados: {}", requestId, phones.size());

        log.info("[{}] Encriptando password (BCrypt)...", requestId);
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        log.info("[{}] Generando JWT...", requestId);
        String token = jwtService.generateToken(request.getEmail());
        log.info("[{}] JWT generado (parcial): {}...", requestId, safeTokenPrefix(token, 12));

        log.info("[{}] Persistiendo usuario en BD...", requestId);
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setPhones(phones);
        user.setToken(token);

        User saved = userRepository.save(user);
        log.info("[{}] Usuario persistido OK: id={}", requestId, saved.getId());

        return UserMapper.toResponse(saved);
    }

    private Phone toEntity(PhoneRequest p) {
        Phone phone = new Phone();
        phone.setNumber(p.getNumber());
        phone.setCitycode(p.getCitycode());
        phone.setContrycode(p.getContrycode());
        return phone;
    }

    private String safeTokenPrefix(String token, int n) {
        if (token == null) return "null";
        return token.length() <= n ? token : token.substring(0, n);
    }
}
