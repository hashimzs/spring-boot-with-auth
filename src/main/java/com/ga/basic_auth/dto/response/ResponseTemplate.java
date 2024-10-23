package com.ga.basic_auth.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;


@Getter
@Setter
public class ResponseTemplate extends ResponseEntity {

    private int status ; private String message; private String objectType ;
    public <T> ResponseTemplate(int status , String message, String objectType , T object) {
        super(createBody(message,objectType,object), HttpStatusCode.valueOf(status));
        this.status=status;
    }

    public ResponseTemplate(HttpStatusCode status , String message) {
        super(createBody(message), status);
    }

    private static HashMap createBody(String message){
        HashMap<String,String> bodyMap=new HashMap<>();
        bodyMap.put("message", message);
        return bodyMap;
    }
    private static <T> HashMap<String, T> createBody(T message, String objectType,T object){
        HashMap<String, T> bodyMap=new HashMap<>();
        bodyMap.put("message", message);
        bodyMap.put(objectType,object);
        return bodyMap;
    }

    public static <T> HashMap<String,T> getBody(ResponseEntity re){
        return (HashMap<String, T>) re.getBody();
    }

}
