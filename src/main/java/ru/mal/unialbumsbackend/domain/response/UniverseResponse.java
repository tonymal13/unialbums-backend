package ru.mal.unialbumsbackend.domain.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniverseResponse {

    private Map<String,String> data;

    private String message;

    public void addData(String key, String value) {
        data.put(key, value);
    }
}
