package com.y_lab.y_lab.web.order.service.chain;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.handler.FilterHandler;

import java.util.List;

public class ClientFilterChain extends FilterHandler {
    private static final String FILTER_NAME = "client";

    @Override
    protected FilterHandler checkFilterName(String filterName) {
        if (FILTER_NAME.equals(filterName)) {
            return this;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> filter(String argument) throws IllegalArgumentException {
        try {
            String clientId = argument.trim();
            Long idLong = Long.parseLong(clientId);
            return orderService.findOrdersByCustomer(idLong);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }
}
