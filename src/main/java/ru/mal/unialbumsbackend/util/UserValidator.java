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

        Optional<User> user = userService.findByUsernameOptional(userDto.getUsername());

        if(user.isEmpty()){
            message= ("Вы успешно зарегестрировались");
            message=validatePassword(message,userDto.getPassword());
            message=validateUsername(message,userDto.getUsername());
        }
        else {

            message = ("Такой пользователь уже существует");
        }
        return message;
    }

    public String validateForEdit(UserDto userDto){
        String message= "";

        Optional <User> user = userService.findByUsernameOptional(userDto.getUsername());

        if(user.isEmpty()){
            message= ("Данные успешно обновлены");
            message=validatePassword(message,userDto.getPassword());
            message=validateUsername(message,userDto.getUsername());
        }
        return message;
    }

    private String validatePassword(String currentMessage,String password){
        if (password!=null) {
            if (password.length() < 1 || password.length() > 20) {
                currentMessage = "Пароль должен быть от 1 до 20 символов :)";
            }
        }
        return currentMessage;
    }

    private String validateUsername(String currentMessage,String username){
        if(username.length()<1 || username.length()>20) {
            currentMessage = "Логин должен быть от 1 до 20 символов :)";
        }
        return currentMessage;
    }

}
