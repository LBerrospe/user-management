package com.drawsforall.user.management.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ApiResponse{

    private int status;
    private String message;
    private Object result;


    public ApiResponse(HttpStatus status, String message, Object result){

        this.status=status.value();
        this.message=message;
        this.result=result;
    }

    @Override
    public String toString() {
        return "ApiResponse [statusCode=" + status + ", message=" + message +"]";
    }

}
