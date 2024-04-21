package ru.mal.unialbumsbackend.web.dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UniverseResponse {

    private List<HashMap<String, String>> data;

    private String message;

    public void addData(HashMap<String, String> map, String key, String value) {
        map.put(key,value);
    }

    public void addMap(HashMap<String,String> map){
        data.add(map);
    }

    public void removeFromData(String refreshToken) {
        data.get(0).remove(refreshToken);
    }
}
