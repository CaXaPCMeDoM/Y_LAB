package com.y_lab.y_lab.web.order.status.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.dto.request.UpdateOrderStatusRequestDto;
import com.y_lab.y_lab.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/orders/update-status")
public class OrderStatusUpdateServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService = ServiceContainer.getOrderService();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UpdateOrderStatusRequestDto dto = objectMapper.readValue(req.getInputStream(), UpdateOrderStatusRequestDto.class);

            orderService.changeStatus(dto.orderId(), dto.orderStatus());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Статус заказа успешно изменен.\"}");
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Некорректный статус заказа.\"}");
        } catch (InvalidFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Что ты ввёл? Введи нормальный order_status в upperCase. Будь человеком!\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Ошибка при изменении статуса заказа.\"}");
        }
    }
}
