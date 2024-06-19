package ru.mal.unialbumsbackend.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;

import java.util.Optional;


@Component
@AllArgsConstructor
public class UserValidator {

    private final UserService userService;

    public String validateForRegister(UserDto userDto) {
        Optional<User> user = userService.findByUsernameOptional(userDto.getUsername());

        if (user.isEmpty()) {
            String message = "Вы успешно зарегистрировались";
            message = validatePassword(message, userDto.getPassword());
            return validateUsername(message, userDto.getUsername());
        } else {
            return "Такой пользователь уже существует";
        }
    }

    public String validateForEdit(UserDto userDto) {
        Optional<User> user = userService.findByUsernameOptional(userDto.getUsername());

        if (user.isPresent()) {
            String message = "Пользователь с таким ником уже существует";
            return validateUsername(message, userDto.getUsername());
        } else {
            return "Данные успешно обновлены";
        }
    }

    private String validatePassword(String currentMessage, String password) {
        if (password != null && (password.length() < 1 || password.length() > 20)) {
            return "Пароль должен быть от 1 до 20 символов :)";
        }
        return currentMessage;
    }

    private String validateUsername(String currentMessage, String username) {
        if (username.length() < 1 || username.length() > 20) {
            return "Логин должен быть от 1 до 20 символов :)";
        }
        return currentMessage;
    }

}
