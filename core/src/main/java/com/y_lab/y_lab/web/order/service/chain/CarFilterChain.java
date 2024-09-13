package com.y_lab.y_lab.web.order.service.chain;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.handler.FilterHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarFilterChain extends FilterHandler {
    private static final String FILTER_NAME = "car";


    @Override
    protected boolean isApplicable(String filterName) {
        return FILTER_NAME.equals(filterName);
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
