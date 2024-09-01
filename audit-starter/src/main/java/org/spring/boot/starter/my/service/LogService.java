package org.spring.boot.starter.my.service;

import entity.ActionType;

public interface LogService {
    void log(long userId, ActionType actionType);
}
