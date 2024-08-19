package com.y_lab.y_lab.web.order.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.web.order.service.chain.handler.ChainFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderFilterServletTest {

    private OrderFilterServlet orderFilterServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;
    private ChainFilter chainFilter;

    @BeforeEach
    void setUp() throws Exception {
        // Инициализируем объекты перед каждым тестом
        chainFilter = mock(ChainFilter.class);
        orderFilterServlet = new OrderFilterServlet(chainFilter, new ObjectMapper());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_Success() throws Exception {
        // Подготавливаем параметры запроса
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("status", new String[]{"COMPLETED"});
        when(request.getParameterMap()).thenReturn(parameterMap);

        // Мокаем цепочку фильтров
        when(chainFilter.assemblingTheChain(eq("status"), any(String[].class)))
                .thenReturn(List.of(new Order())); // Вернем mock-список заказов

        // Вызываем doGet
        orderFilterServlet.doGet(request, response);

        // Проверяем, что ответ был отправлен
        verify(response).setContentType("application/json");
        verify(writer).write(contains("[{")); // Проверяем, что в ответе JSON
    }

    @Test
    void testDoGet_InvalidParameter() throws Exception {
        // Подготавливаем параметры запроса с неверным параметром
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("status", new String[]{"INVALID_STATUS"});
        when(request.getParameterMap()).thenReturn(parameterMap);

        // Настраиваем исключение
        when(chainFilter.assemblingTheChain(eq("status"), any(String[].class)))
                .thenThrow(new IllegalArgumentException());

        // Вызываем doGet
        orderFilterServlet.doGet(request, response);

        // Проверяем, что возвращается статус 400 и сообщение об ошибке
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(contains("{\"error\": \"Invalid filter parameter.\"}"));
    }

    @Test
    void testDoGet_ParseException() throws Exception {
        // Подготавливаем параметры запроса с неверным форматом даты
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("dateRange", new String[]{"invalid-date"});
        when(request.getParameterMap()).thenReturn(parameterMap);

        // Настраиваем исключение ParseException
        when(chainFilter.assemblingTheChain(eq("dateRange"), any(String[].class)))
                .thenThrow(new ParseException("Invalid date format", 0));

        // Вызываем doGet
        orderFilterServlet.doGet(request, response);

        // Проверяем, что возвращается статус 400 и сообщение об ошибке
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write(contains("{\"error\": \"Invalid date format.\"}"));
    }

    @Test
    void testDoGet_InternalServerError() throws Exception {
        // Подготавливаем параметры запроса
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("status", new String[]{"COMPLETED"});
        when(request.getParameterMap()).thenReturn(parameterMap);

        // Настраиваем неожиданное исключение
        when(chainFilter.assemblingTheChain(eq("status"), any(String[].class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Вызываем doGet
        orderFilterServlet.doGet(request, response);

        // Проверяем, что возвращается статус 500 и сообщение об ошибке
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(writer).write(contains("{\"error\": \"An unexpected error occurred.\"}"));
    }
}