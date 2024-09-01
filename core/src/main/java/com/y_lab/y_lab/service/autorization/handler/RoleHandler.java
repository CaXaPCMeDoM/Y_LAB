package com.y_lab.y_lab.service.autorization.handler;

import entity.enums.Role;
import org.springframework.stereotype.Component;

@Component
public abstract class RoleHandler {
    protected RoleHandler roleHandler;

    public RoleHandler setNextHandler(RoleHandler roleHandler) {
        this.roleHandler = roleHandler;
        return this;
    }

    public abstract boolean handlerRole(Role role);
}
