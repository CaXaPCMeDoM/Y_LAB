package com.y_lab.y_lab.entity;

import com.y_lab.y_lab.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long userId;
    private String username;
    private String password;
    private Role role;
}
