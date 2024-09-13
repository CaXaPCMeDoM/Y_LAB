package com.y_lab.y_lab.exception;

public class InvalidUsernameOrPassword extends Exception {
    public InvalidUsernameOrPassword() {
        super("Неверный логин или пароль!");
    }
}
