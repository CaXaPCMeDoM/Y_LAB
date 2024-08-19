package com.y_lab.y_lab.web.order.service.chain.handler;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.service.OrderService;

import java.text.ParseException;
import java.util.List;

public abstract class FilterHandler {
    protected FilterHandler filterHandler;
    protected OrderService orderService;

    public FilterHandler setNextHandler(FilterHandler filterHandler) {
        this.filterHandler = filterHandler;
        return this;
    }

    public List<Order> handle(String filterName, String[] arguments) {
        FilterHandler checkFilterName = filterHandler.checkFilterName(filterName);
        if (checkFilterName == null) {
            return filterHandler.handle(filterName, arguments);
        } else {
            List<Order> filteredOrders = filterHandler.filter(arguments[0]);

            // Проходимся по остальным аргументам конкретного фильтра
            for (int i = 1; i < arguments.length; i++) {
                List<Order> currentFilterResult = filterHandler.filter(arguments[i]);
                filteredOrders.retainAll(currentFilterResult); // Сохраняем только пересекающиеся элементы
            }

            return filteredOrders;
        }
    }

    protected abstract FilterHandler checkFilterName(String filterName);

    protected abstract List<Order> filter(String argument) throws IllegalArgumentException;
}
