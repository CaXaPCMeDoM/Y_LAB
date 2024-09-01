package com.y_lab.y_lab.web.order.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.dto.request.OrderCreateRequestDto;
import com.y_lab.y_lab.entity.dto.request.UpdateOrderStatusRequestDto;
import com.y_lab.y_lab.entity.enums.OrderStatus;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.service.OrderService;
import com.y_lab.y_lab.web.order.OrderController;
import com.y_lab.y_lab.web.order.service.chain.chain.ChainFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;

    @Mock
    private ChainFilter chainFilter;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper(); // Initialize ObjectMapper here
    }

    @Test
    public void testCreateOrderSuccess() throws Exception {
        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Заказ успешно создан."));
    }

    @Test
    public void testCreateOrderConflict() throws Exception {
        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(1L, 1L);
        doThrow(new OrderForTheCarAlreadyExists()).when(orderService).createOrder(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Заказ на данный автомобиль уже существует."));
    }

    @Test
    public void testCreateOrderBadRequest() throws Exception {
        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(1L, 1L);
        doThrow(new RuntimeException()).when(orderService).createOrder(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибка при создании заказа."));
    }

    @Test
    public void testDeleteOrderSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Заказ успешно отменен."));
    }

    @Test
    public void testDeleteOrderBadRequest() throws Exception {
        doThrow(new NumberFormatException()).when(orderService).canceledOrder(anyLong());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete")
                        .param("id", "1"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contentType = result.getResponse().getContentType();
        Assertions.assertEquals("application/json", contentType);

        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/delete")
                        .param("id", "12345")) // Use a valid ID if needed
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректный идентификатор заказа."));
    }

    @Test
    public void testFilterOrdersBadRequest() throws Exception {
        doThrow(new ParseException("Invalid date format", 0)).when(chainFilter).assemblingTheChain(anyString(), any(String[].class));

        mockMvc.perform(get("/orders/filter")
                        .param("date", "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid date format."));
    }

    @Test
    public void testFilterOrdersSuccess() throws Exception {
        List<Order> orders = List.of(
                new Order(null, 1L, 1L, OrderStatus.CREATED, new Timestamp(System.currentTimeMillis())),
                new Order(null, 2L, 2L, OrderStatus.IN_PROGRESS, new Timestamp(System.currentTimeMillis() - 100000))
        );
        when(chainFilter.assemblingTheChain(anyString(), any(String[].class))).thenReturn(orders);

        mockMvc.perform(get("/orders/filter")
                        .param("status", OrderStatus.CREATED.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value(1))
                .andExpect(jsonPath("$[1].carId").value(2))
                .andExpect(jsonPath("$[0].status").value(OrderStatus.CREATED.toString()))
                .andExpect(jsonPath("$[1].status").value(OrderStatus.IN_PROGRESS.toString()));
    }

    @Test
    public void testFindOrderNotFound() throws Exception {
        when(orderService.searchOrderById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/orders/find")
                        .param("id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Заказ не найден."));
    }

    @Test
    public void testUpdateOrderStatusSuccess() throws Exception {
        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto(1L, OrderStatus.CREATED);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Статус заказа успешно изменен."));
    }

    @Test
    public void testUpdateOrderStatusBadRequest() throws Exception {
        UpdateOrderStatusRequestDto requestDto = new UpdateOrderStatusRequestDto(1L, OrderStatus.CREATED);
        doThrow(new IllegalArgumentException()).when(orderService).changeStatus(anyLong(), any(OrderStatus.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректный статус заказа."));
    }
}
