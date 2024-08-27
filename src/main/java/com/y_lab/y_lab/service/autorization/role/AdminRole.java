package com.y_lab.y_lab.service.autorization.role;

import com.y_lab.y_lab.entity.enums.Role;
import com.y_lab.y_lab.service.autorization.handler.RoleHandler;
import org.springframework.stereotype.Component;

@Component
public class AdminRole extends RoleHandler {
    @Override
    public boolean handlerRole(Role role) {
        if (Role.ADMIN.equals(role)) { /* TODO (в тз не прописана логика, по которой мы
                                        TODO присваиваем роли(хотя логично было бы по дополнительному паролю),
                                        TODO поэтому пока просто возращает true) */
            return true;
        } else {
            if (roleHandler == null) {
                return false;
            } else {
                return roleHandler.handlerRole(role);
            }
        }
    }
}
