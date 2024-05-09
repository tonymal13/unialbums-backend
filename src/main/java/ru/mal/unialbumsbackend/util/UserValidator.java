package ru.mal.unialbumsbackend.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.exception.UserNotFoundException;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;


@Component
@AllArgsConstructor
public class UserValidator {

    private final UserService userService;

    public String validateForRegister(UserDto userDto){
        String message= "";
        try {
            User user = userService.findByLogin(userDto.getUsername());
        }
        catch (UserNotFoundException e){
            message= ("Вы успешно зарегестрировались");
            message=validatePassword(message,userDto.getPassword());
            message=validateUsername(message,userDto.getUsername());
            return message;
        }

        message= ("Такой пользователь уже существует");

        return message;
    }

    public String validateForEdit(UserDto userDto,long userId){
        String message= "";
        User user=userService.findByLogin(userDto.getUsername());
        message= ("Данные успешно обновлены");
        if(user.getId()!=userId){
            message= ("Такой пользователь уже существует");
        }
        message=validatePassword(message,userDto.getPassword());
        message=validateUsername(message,userDto.getUsername());
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
