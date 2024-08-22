package com.y_lab.y_lab.web.order.service.chain.handler;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.CarFilterChain;
import com.y_lab.y_lab.web.order.service.chain.ClientFilterChain;
import com.y_lab.y_lab.web.order.service.chain.DateFilterHandler;
import com.y_lab.y_lab.web.order.service.chain.StatusFilterChain;

import java.text.ParseException;
import java.util.List;

public class ChainFilter {
    private final FilterHandler clientHandler;
    private final FilterHandler statusHandler;
    private final FilterHandler carHandler;
    private final FilterHandler dateHandler;

    public ChainFilter() {

        clientHandler = new ClientFilterChain();
        statusHandler = new StatusFilterChain();
        dateHandler = new DateFilterHandler();
        carHandler = new CarFilterChain();
        statusHandler.setNextHandler(
                dateHandler.setNextHandler(
                        clientHandler.setNextHandler(
                                carHandler)));
    }

    public List<Order> assemblingTheChain(String filterNames, String[] arguments) throws ParseException {
        return statusHandler.handle(filterNames, arguments);
    }
}
