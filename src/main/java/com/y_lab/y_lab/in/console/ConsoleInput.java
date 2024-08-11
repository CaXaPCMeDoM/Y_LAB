package com.y_lab.y_lab.in.console;

import com.y_lab.y_lab.in.Input;

import java.util.Scanner;

public class ConsoleInput implements Input {
    private static final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    public int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    @Override
    public long readLong() {
        return Long.parseLong(scanner.nextLine());
    }

    public double readDouble() {
        return Double.parseDouble(scanner.nextLine());
    }
}
