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

/**
 * Класс UserManagement предоставляет функциональность для управления пользователями,
 * включая регистрацию, авторизацию и просмотр списка пользователей.
 */
public class UserManagement {
    private final UserService userService;
    private final Printer printer;
    private final Input input;

    /**
     * Создает новый экземпляр UserManagement с указанными сервисами
     *
     * @param userService сервис для управления пользователями
     * @param printer     сервис для вывода сообщений пользователю
     * @param input       сервис для чтения ввода пользователя
     */
    public UserManagement(UserService userService, Printer printer, Input input) {
        this.userService = userService;
        this.printer = printer;
        this.input = input;
    }

    /**
     * Регистрирует нового пользователя, запрашивая у него имя пользователя, пароль и роль.
     * Выводит соответствующее сообщение в случае успешной регистрации или ошибки.
     */
    public void registerUser() {
        printer.print("Введите имя пользователя:");
        String username = input.readLine();
        printer.print("Введите пароль:");
        String password = input.readLine();
        printer.print("Введите роль (ADMIN, MANAGER, CLIENT):");
        String role = input.readLine();
        Role roleUser;
        try {
            roleUser = Role.valueOf(role.toUpperCase());

            User user = new User(null, username, password, roleUser);

            userService.registerUser(user);
            printer.print("Пользователь успешно зарегистрирован.");
        } catch (AuthorizationFailedForTheRole e) {
            printer.print("Авторизация для данной роли не удалась.");
        } catch (UserIsAlreadyRegistered e) {
            printer.print("Пользователь уже зарегистрирован.");
        } catch (IllegalArgumentException ex) {
            printer.print("Введенная роль не поддерживается");
        }
    }

    /**
     * Авторизует пользователя, запрашивая у него имя пользователя и пароль.
     * Выводит соответствующее сообщение в случае успешной авторизации или ошибки.
     */
    public void loginUser() {
        printer.print("Введите имя пользователя:");
        String username = input.readLine();
        printer.print("Введите пароль:");
        String password = input.readLine();

        try {
            userService.authorization(username, password);
            printer.print("Пользователь успешно вошел в систему.");
        } catch (InvalidUsernameOrPassword e) {
            printer.print("Неверное имя пользователя или пароль.");
        }
    }

    /**
     * Отображает список всех зарегистрированных пользователей.
     */
    public void viewUsers() {
        List<User> users = userService.getAll();
        for (User user : users) {
            printer.print(user.toString());
        }
    }
}

