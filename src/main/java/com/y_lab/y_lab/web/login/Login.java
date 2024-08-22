package com.y_lab.y_lab.web.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.dto.response.UserResponseDto;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.mapper.UserMapper;
import com.y_lab.y_lab.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public Login() {
        this.objectMapper = new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true);
        this.userService = ServiceContainer.getUserService();
        userMapper = UserMapper.INSTANCE;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = objectMapper.readValue(req.getInputStream(), User.class);

            User registeredUser = userService.authorization(user.getUsername(), user.getPassword());

            UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);

            String jsonResponse = objectMapper.writeValueAsString(userResponseDto);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            resp.getWriter().write(jsonResponse);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (InvalidUsernameOrPassword e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
