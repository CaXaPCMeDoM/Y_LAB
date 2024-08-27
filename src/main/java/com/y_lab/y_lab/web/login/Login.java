package com.y_lab.y_lab.web.login;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.dto.response.UserResponseDto;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.mapper.UserMapper;
import com.y_lab.y_lab.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class Login {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> doPost(@RequestBody User user) {
        try {
            User registeredUser = userService.authorization(user.getUsername(), user.getPassword());

            UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);

            return ResponseEntity.ok(userResponseDto);
        } catch (InvalidUsernameOrPassword e) {
            return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(Map.of("error", "Invalid username or password."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request."));
        }
    }
}
