package com.y_lab.y_lab.exception;

public class UserIsAlreadyRegistered extends Exception{
    public UserIsAlreadyRegistered(){
        super("Пользователь уже зарегистрирован");
    }
}
