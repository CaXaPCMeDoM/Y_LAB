package com.y_lab.y_lab.web.order.service.chain;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.handler.FilterHandler;

import java.util.List;

public class CarFilterChain extends FilterHandler {
    private static final String FILTER_NAME = "car";

    @Override
    protected FilterHandler checkFilterName(String filterName) {
        if (FILTER_NAME.equals(filterName)) {
            return this;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> filter(String argument) throws IllegalArgumentException{
        try {
            String carId = argument.trim();
            Long carIdLong = Long.parseLong(carId);
            return orderService.findOrdersByCar(carIdLong);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }
}
