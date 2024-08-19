package com.y_lab.y_lab.web.order.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.handler.ChainFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/orders/filter")
public class OrderFilterServlet extends HttpServlet {
    private final ChainFilter chainFilter;
    private final ObjectMapper objectMapper;

    public OrderFilterServlet() {
        chainFilter = new ChainFilter();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Map<String, String[]> parameterMap = request.getParameterMap();
        List<Order> filteredOrders = new ArrayList<>();

        try {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String filterName = entry.getKey();
                String[] arguments = entry.getValue();

                // Применяем цепочку фильтров
                List<Order> result = chainFilter.assemblingTheChain(filterName, arguments);

                // Если это первый результат, добавляем его в список
                if (filteredOrders.isEmpty()) {
                    filteredOrders.addAll(result);
                } else {
                    // Оставляем только пересекающиеся элементы между фильтрами
                    filteredOrders.retainAll(result);
                }
            }

            // Отправляем отфильтрованные заказы в ответ
            String jsonResponse = objectMapper.writeValueAsString(filteredOrders);
            response.getWriter().write(jsonResponse);
        } catch (ParseException e) {
            // Обработка ошибки формата даты
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid date format.\"}");
        } catch (IllegalArgumentException e) {
            // Обработка ошибки неверного параметра (например, некорректный статус)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid filter parameter.\"}");
        } catch (Exception e) {
            // Обработка других ошибок
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An unexpected error occurred.\"}");
        }
    }
}
