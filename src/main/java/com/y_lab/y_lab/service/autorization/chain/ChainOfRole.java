package com.y_lab.y_lab.service.autorization.chain;

import com.y_lab.y_lab.entity.enums.Role;
import com.y_lab.y_lab.service.autorization.role.AdminRole;
import com.y_lab.y_lab.service.autorization.role.ManagerRole;
import com.y_lab.y_lab.service.autorization.role.UserRole;
import com.y_lab.y_lab.service.autorization.handler.RoleHandler;

public class ChainOfRole {
    private RoleHandler adminRole = new AdminRole();
    private RoleHandler managerRole = new ManagerRole();
    private RoleHandler userRole = new UserRole();

    public ChainOfRole() {
    }

    public boolean assemblingTheChain(Role role) {
        return adminRole.handlerRole(role);
    }
}
