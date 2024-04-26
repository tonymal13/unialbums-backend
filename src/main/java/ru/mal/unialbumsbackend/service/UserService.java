package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.web.dto.auth.RegRequest;
import ru.mal.unialbumsbackend.repositories.UserRepository;


import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegRequest regRequest) {
        User user= enrich(regRequest);
        userRepository.save(user);
    }

    private User enrich(RegRequest request) {
        User user=new User();
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode( request.getPassword()));
        user.setLogin(request.getLogin());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setAvatar("");
        return user;
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}