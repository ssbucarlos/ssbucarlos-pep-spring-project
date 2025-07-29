package com.example.exception;

public class BadAccountException extends Exception{
    public BadAccountException(String message){
        super(message);
    }
}
