package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.util.UserValidator;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;
import ru.mal.unialbumsbackend.repositories.UserRepository;


import java.util.ArrayList;
import java.util.Optional;

import static ru.mal.unialbumsbackend.web.controller.AlbumsController.decodeJWTGetHeader;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserDto userDto) {
        User user= toEntity(userDto);
        save(user);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User toEntity(UserDto request) {
        User user=new User();
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode( request.getPassword()));
        user.setUsername(request.getUsername());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setAvatar("");
        return user;
    }

    public void toDto(User user, UserDto request){
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByLogin(String username) {
        return userRepository.findByUsername(username);
    }

}