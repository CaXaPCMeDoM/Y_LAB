package com.y_lab.y_lab.web.order.find;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/orders/find")
public class OrderFindServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService = ServiceContainer.getOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long orderId = Long.parseLong(req.getParameter("id"));
            Order order = orderService.searchOrderById(orderId);

            if (order != null) {
                String jsonResponse = objectMapper.writeValueAsString(order);
                resp.setContentType("application/json");
                resp.getWriter().write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Заказ не найден.\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Ошибка при поиске заказа.\"}");
        }
    }
}
