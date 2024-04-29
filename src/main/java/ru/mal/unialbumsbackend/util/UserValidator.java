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

    public String validateForRegister(UserDto userDto){
        String message= "";

        Optional<User> user=userService.findByLogin(userDto.getUsername());
        if (user.isPresent()) {
                message= ("Такой пользователь уже существует");
        } else {
            message= ("Вы успешно зарегестрировались");
        }

        message=validatePassword(message,userDto.getPassword());
        message=validateUsername(message,userDto.getUsername());

        return message;
    }

    public String validateForEdit(UserDto userDto,long userId){
        String message= "";
        Optional<User> user=userService.findByLogin(userDto.getUsername());
        message= ("Данные успешно обновлены");
        if (user.isPresent()) {
            if(user.get().getId()!=userId){
                message= ("Такой пользователь уже существует");
            }
        }
        message=validatePassword(message,userDto.getPassword());
        message=validateUsername(message,userDto.getUsername());
        return message;
    }

    private String validatePassword(String currentMessage,String password){
        if (password!=null) {
            if (password.length() < 1 || password.length() > 20)
                currentMessage = "Пароль должен быть от 1 до 20 символов :)";
        }
        return currentMessage;
    }

    private String validateUsername(String currentMessage,String username){
        if(username.length()<1 || username.length()>20)
            currentMessage="Логин должен быть от 1 до 20 символов :)";
        return currentMessage;
    }

}
