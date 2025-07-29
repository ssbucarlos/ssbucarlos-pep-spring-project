package com.example.exception;

public class BadMessageException extends Exception{
    public BadMessageException(String message){
        super(message);
    }
}
