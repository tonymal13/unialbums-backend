package ru.mal.unialbumsbackend.domain.response;

import lombok.*;

import java.util.ArrayList;
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
//        ArrayList<String> list=new ArrayList<>();
        map.put(key,value);
//        map2.put(key, value);
    }

    public void addMap(HashMap<String,String> map){
        data.add(map);
    }

}
