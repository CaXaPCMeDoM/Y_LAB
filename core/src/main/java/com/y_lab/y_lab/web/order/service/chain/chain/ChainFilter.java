package com.y_lab.y_lab.web.order.service.chain.chain;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.CarFilterChain;
import com.y_lab.y_lab.web.order.service.chain.ClientFilterChain;
import com.y_lab.y_lab.web.order.service.chain.DateFilterHandler;
import com.y_lab.y_lab.web.order.service.chain.StatusFilterChain;
import com.y_lab.y_lab.web.order.service.chain.handler.FilterHandler;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

@Component
public class ChainFilter {
    private final FilterHandler statusHandler;

    public ChainFilter(ClientFilterChain clientFilterChain,
                       StatusFilterChain statusFilterChain,
                       DateFilterHandler dateFilterHandler,
                       CarFilterChain carFilterChain) {

        statusFilterChain.setNextHandler(
                dateFilterHandler.setNextHandler(
                        clientFilterChain.setNextHandler(
                                carFilterChain)));
        this.statusHandler = statusFilterChain;
    }

    public List<Order> assemblingTheChain(String filterNames, String[] arguments) throws ParseException {
        return statusHandler.handle(filterNames, arguments);
    }
}
