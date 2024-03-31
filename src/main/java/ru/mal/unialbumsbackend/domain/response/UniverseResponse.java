package ru.mal.unialbumsbackend.domain.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniverseResponse {

    private Map<String,String> data;

    private String message;

    private int code;

    public void addData(String key, String value) {
        data.put(key, value);
    }
}
