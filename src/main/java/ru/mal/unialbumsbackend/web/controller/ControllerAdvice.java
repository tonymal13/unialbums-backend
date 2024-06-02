package ru.mal.unialbumsbackend.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.exception.ImageUploadException;
import ru.mal.unialbumsbackend.exception.UserNotFoundException;
import ru.mal.unialbumsbackend.exception.ValidationException;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    private ResponseEntity<BackendResponse> handleException(ImageUploadException e){
        BackendResponse universeResponse=new BackendResponse();
        universeResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(universeResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<BackendResponse> handleException(ValidationException e){
        BackendResponse universeResponse=new BackendResponse();
        universeResponse.setMessage( e.getMessage());
        return new ResponseEntity<>(universeResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<BackendResponse> handleException(UserNotFoundException e){
        BackendResponse universeResponse=new BackendResponse();
        universeResponse.setMessage( e.getMessage());
        return new ResponseEntity<>(universeResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<BackendResponse> handleException(AuthException e){
        BackendResponse universeResponse=new BackendResponse();
        universeResponse.setMessage( e.getMessage());
        return new ResponseEntity<>(universeResponse, HttpStatus.FORBIDDEN);
    }

}
