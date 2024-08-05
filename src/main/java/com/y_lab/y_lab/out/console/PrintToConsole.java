package com.y_lab.y_lab.out.console;

import com.y_lab.y_lab.out.Printer;

public class PrintToConsole implements Printer {
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
