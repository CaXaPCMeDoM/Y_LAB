package com.y_lab.y_lab.web.order.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.dto.request.OrderCreateRequestDto;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/orders/create")
public class OrderCreateServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public OrderCreateServlet() {
        orderService = ServiceContainer.getOrderService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            OrderCreateRequestDto orderCreateRequestDto = objectMapper.readValue(req.getInputStream(), OrderCreateRequestDto.class);

            orderService.createOrder(orderCreateRequestDto.carId(), orderCreateRequestDto.customerId());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Заказ успешно создан.\"}");
        } catch (OrderForTheCarAlreadyExists e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\": \"Заказ на данный автомобиль уже существует.\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Ошибка при создании заказа.\"}");
        }
    }
}
