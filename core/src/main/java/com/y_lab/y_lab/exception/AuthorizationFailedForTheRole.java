package com.y_lab.y_lab.exception;

public class AuthorizationFailedForTheRole extends Exception{
    public AuthorizationFailedForTheRole(){
        super("Авторизация не прошла по роли");
    }
}
