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
            message= ("Добавлено в БД");
        }
        String regex = "\\p{Lu}\\p{L}{1,20}";

        if (userDto.getPassword().length() < 1 || userDto.getPassword().length() > 20)
            message = "Пароль должен быть от 1 до 20 символов :)";
        else if(userDto.getUsername().length()<1)
            message="Логин должен больше 1 до 20 символов :)";
        else if(!userDto.getFirstName().matches(regex))
            message="Имя должно быть в формате: Иван";
        else if(!userDto.getLastName().matches(regex))
            message="Фамилия должна быть в формате: Иванов";
        return message;
    }

    public String validateForEdit(UserDto userDto){
        String message= "";

        String regex = "\\p{Lu}\\p{L}{1,20}";

        if (userDto.getPassword()!=null) {

            if (userDto.getPassword().length() < 1 || userDto.getPassword().length() > 20)
                message = "Пароль должен быть от 1 до 20 символов :)";
        }
        else if(userDto.getUsername().length()<1)
            message="Логин должен больше 1 до 20 символов :)";
        else if(!userDto.getFirstName().matches(regex))
            message="Имя должно быть в формате: Иван";
        else if(!userDto.getLastName().matches(regex))
            message="Фамилия должна быть в формате: Иванов";
        else{
            message="Данные успешно обновлены";
        }
        return message;
    }

}
