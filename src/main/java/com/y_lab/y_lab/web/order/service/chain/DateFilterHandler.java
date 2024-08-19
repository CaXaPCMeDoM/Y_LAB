package com.y_lab.y_lab.web.order.service.chain;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.handler.FilterHandler;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateFilterHandler extends FilterHandler {
    private final static String FILTER_NAME = "date";

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
        List<String> dateStrings = List.of(argument.split("to"));

        if (dateStrings.size() != 2) {
            throw new IllegalArgumentException();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Timestamp startDate;
        Timestamp endDate;

        try {
            Date startParsedDate = dateFormat.parse(dateStrings.get(0).trim());
            Date endParsedDate = dateFormat.parse(dateStrings.get(1).trim());
            startDate = new Timestamp(startParsedDate.getTime());
            endDate = new Timestamp(endParsedDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: 'yyyy-MM-dd HH:mm:ss.SSS'", e);
        }

        return orderService.findOrdersByDate(startDate, endDate);
    }
}
