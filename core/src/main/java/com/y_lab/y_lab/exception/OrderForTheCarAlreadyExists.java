package com.y_lab.y_lab.exception;

public class OrderForTheCarAlreadyExists extends Exception {
    public OrderForTheCarAlreadyExists() {
        super("Заказ на машину уже существует");
    }

    OrderForTheCarAlreadyExists(String message) {
        super(message);
    }
}
