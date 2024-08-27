package com.y_lab.y_lab.web.order.service.chain.handler;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.service.OrderService;

import java.text.ParseException;
import java.util.List;

public abstract class FilterHandler {
    protected FilterHandler nextHandler;
    protected OrderService orderService;

    public FilterHandler setNextHandler(FilterHandler filterHandler) {
        this.nextHandler = filterHandler;
        return this;
    }

    public List<Order> handle(String filterName, String[] arguments) throws ParseException {
        // Проверяем, применим ли текущий фильтр к переданным параметрам
        if (isApplicable(filterName)) {
            List<Order> filteredOrders = filter(arguments[0]);

            // Если есть следующий обработчик, передаем ему управление
            if (nextHandler != null) {
                List<Order> nextFilteredOrders = nextHandler.handle(filterName, arguments);
                filteredOrders.retainAll(nextFilteredOrders); // Сохраняем пересечения между результатами фильтров
            }

            return filteredOrders;
        } else if (nextHandler != null) {
            // Передаем управление следующему фильтру в цепочке
            return nextHandler.handle(filterName, arguments);
        } else {
            // Если ни один фильтр не применим, возвращаем пустой список
            return List.of();
        }
    }

    protected abstract boolean isApplicable(String filterName);

    protected abstract List<Order> filter(String arguments) throws ParseException;
}
