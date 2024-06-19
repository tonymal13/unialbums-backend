package ru.mal.unialbumsbackend.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class BackendResponse {

    private List<HashMap<String, String>> data;

    private String message;

    public void addData(HashMap<String, String> map, String key, String value) {
        map.put(key,value);
    }

    public void addMap(HashMap<String,String> map){
        data.add(map);
    }

    public static BackendResponse initializeResponse() {
        BackendResponse universeResponse=new BackendResponse();
        universeResponse.setData(new ArrayList<>());
        return universeResponse;
    }
}
