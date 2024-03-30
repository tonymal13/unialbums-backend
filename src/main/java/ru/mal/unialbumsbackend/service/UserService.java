package ru.mal.unialbumsbackend.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.domain.requests.RegRequest;
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

    @Transactional
    public void register(RegRequest regRequest) {
        User user= enrich(regRequest);
        userRepository.save(user);
    }

    private User enrich(RegRequest request) {
        User user=new User();
        user.setRole("USER");
        user.setPassword(request.getPassword());
        user.setLogin(request.getLogin());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setAvatar("");
        return user;
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}