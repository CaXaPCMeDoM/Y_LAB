package com.y_lab.y_lab.web.order.service.chain;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.web.order.service.chain.handler.FilterHandler;

import java.util.List;

public class StatusFilterChain extends FilterHandler {
    private static final String FILTER_NAME = "status";

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
        String status = argument.trim();
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        return orderService.findOrdersByStatus(orderStatus);
    }
}