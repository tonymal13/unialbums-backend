package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.exception.UserNotFoundException;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;

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

    public User toEntity(UserDto userDto) {
        User user=new User();
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode( userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setAvatar("");
        return user;
    }

    public void toDto(User user, UserDto userDto){
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("Пользователь не найден"));
    }

    public User findByLogin(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException("Пользователь не найден"));
    }

}