package com.y_lab.y_lab.starter.manager;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.enums.Role;
import com.y_lab.y_lab.exception.AuthorizationFailedForTheRole;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.exception.UserIsAlreadyRegistered;
import com.y_lab.y_lab.in.Input;
import com.y_lab.y_lab.out.Printer;
import com.y_lab.y_lab.service.UserService;

import java.util.List;

public class UserManagement {
    private final UserService userService;
    private final Printer printer;
    private final Input input;

    public UserManagement(UserService userService, Printer printer, Input input) {
        this.userService = userService;
        this.printer = printer;
        this.input = input;
    }

    public void registerUser() {
        printer.print("Enter username:");
        String username = input.readLine();
        printer.print("Enter password:");
        String password = input.readLine();
        printer.print("Enter role (ADMIN, MANAGER, CLIENT):");
        String role = input.readLine();

        User user = new User(null, username, password, Role.valueOf(role.toUpperCase()));

        try {
            userService.registerUser(user);
            printer.print("User registered successfully.");
        } catch (AuthorizationFailedForTheRole e) {
            printer.print("Authorization failed for the role.");
        } catch (UserIsAlreadyRegistered e) {
            printer.print("User is already registered.");
        }
    }

    public void loginUser() {
        printer.print("Enter username:");
        String username = input.readLine();
        printer.print("Enter password:");
        String password = input.readLine();

        try {
            userService.authorization(username, password);
            printer.print("User logged in successfully.");
        } catch (InvalidUsernameOrPassword e) {
            printer.print("Invalid username or password.");
        }
    }

    public void viewUsers() {
        List<User> users = userService.getAll();
        for (User user : users) {
            printer.print(user.toString());
        }
    }
}
