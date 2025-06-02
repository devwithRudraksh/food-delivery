package com.fooddelivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message){
        super(message);

    }
}
