package com.y_lab.y_lab.web.registation;

import com.y_lab.y_lab.entity.User;
import com.y_lab.y_lab.exception.AuthorizationFailedForTheRole;
import com.y_lab.y_lab.exception.UserIsAlreadyRegistered;
import com.y_lab.y_lab.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class Registration {
    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok().build();
        } catch (UserIsAlreadyRegistered e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already registered.");
        } catch (AuthorizationFailedForTheRole e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization failed for the role.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
