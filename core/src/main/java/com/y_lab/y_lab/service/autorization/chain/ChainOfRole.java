package com.y_lab.y_lab.service.autorization.chain;

import com.y_lab.y_lab.service.autorization.role.AdminRole;
import com.y_lab.y_lab.service.autorization.role.ManagerRole;
import com.y_lab.y_lab.service.autorization.role.UserRole;
import com.y_lab.y_lab.service.autorization.handler.RoleHandler;
import entity.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class ChainOfRole {
    private final RoleHandler adminRole;

    public ChainOfRole(AdminRole adminRole,
                       ManagerRole managerRole,
                       UserRole userRole) {
        adminRole.setNextHandler(
                managerRole.setNextHandler(
                        userRole));
        this.adminRole = adminRole;
    }

    public boolean assemblingTheChain(Role role) {
        return adminRole.handlerRole(role);
    }
}
