package com.example.hit_learning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private int code;
    private String message;
    Object data;

    public static ApiResponse success(Object data){
        return new ApiResponse(1000,null,data);
    }
    public static ApiResponse error(int code,String message){
        return new ApiResponse(code,message,null);
    }

}
