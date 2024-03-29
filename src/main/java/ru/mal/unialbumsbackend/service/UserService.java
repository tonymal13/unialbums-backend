package ru.mal.unialbumsbackend.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.repositories.UserRepository;


import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByLogin(login);
    }

}