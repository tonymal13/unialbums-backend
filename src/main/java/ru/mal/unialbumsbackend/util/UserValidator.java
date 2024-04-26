package ru.mal.unialbumsbackend.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.web.dto.auth.RegRequest;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserService userService;

    public String validate(RegRequest request){
        String message= "";
        Optional<User> user=userService.findByLogin(request.getUsername());
        if (user.isPresent()) {
            if(!user.get().getUsername().equals(request.getUsername()))
                message= ("Такой пользователь уже существует");
            else{
                message= ("Данные успешно обновлены");
            }
        } else {
            message= ("Добавлено в БД");
        }
        String regex = "\\p{Lu}\\p{L}{1,20}";

        if (request.getPassword()!=null) {

            if (request.getPassword().length() < 1 || request.getPassword().length() > 20)
                message = "Пароль должен быть от 1 до 20 символов :)";
        }
        else if(request.getUsername().length()<1)
            message="Логин должен больше 1 до 20 символов :)";
        else if(!request.getFirstName().matches(regex))
            message="Имя должно быть в формате: Иван";
        else if(!request.getLastName().matches(regex))
            message="Фамилия должна быть в формате: Иванов";
        return message;
    }

}
