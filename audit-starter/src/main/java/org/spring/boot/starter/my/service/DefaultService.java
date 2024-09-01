package org.spring.boot.starter.my.service;

import entity.ActionType;

public class DefaultService implements LogService{
    @Override
    public void log(long userId, ActionType actionType) {
        System.out.println("userId: " + userId + "Action_type: " + actionType);
    }
}
