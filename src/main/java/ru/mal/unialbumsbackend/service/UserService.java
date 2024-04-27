package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.web.dto.auth.RegRequest;
import ru.mal.unialbumsbackend.repositories.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegRequest regRequest) {
        User user= toEntity(regRequest);
        save(user);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User toEntity(RegRequest request) {
        User user=new User();
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode( request.getPassword()));
        user.setUsername(request.getUsername());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setAvatar("");
        return user;
    }

    public void edit(User user,RegRequest request){
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
    }

    public RegRequest toDto(User user) {
        RegRequest request=new RegRequest();
//        request.setPassword(passwordEncoder.encode( user.getPassword()));
        request.setUsername(request.getUsername());
        request.setLastName(request.getLastName());
        request.setFirstName(request.getFirstName());
        return request;
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByLogin(String username) {
        return userRepository.findByUsername(username);
    }

}