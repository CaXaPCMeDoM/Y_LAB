package com.y_lab.y_lab.web.login;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.entity.dto.response.UserResponseDto;
import com.y_lab.y_lab.exception.InvalidUsernameOrPassword;
import com.y_lab.y_lab.mapper.UserMapper;
import com.y_lab.y_lab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Authenticate a user", description = "Authenticates a user based on provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Invalid username or password", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<?> doPost(@RequestBody User user) {
        try {
            User registeredUser = userService.authorization(user.getUsername(), user.getPassword());
            UserResponseDto userResponseDto = userMapper.toUserResponseDto(registeredUser);
            return ResponseEntity.ok(userResponseDto);
        } catch (InvalidUsernameOrPassword e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid username or password."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request."));
        }
    }
}
