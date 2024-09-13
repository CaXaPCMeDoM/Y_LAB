package com.y_lab.y_lab.web.order;

import com.y_lab.y_lab.entity.Order;
import com.y_lab.y_lab.entity.dto.request.OrderCreateRequestDto;
import com.y_lab.y_lab.entity.dto.request.UpdateOrderStatusRequestDto;
import com.y_lab.y_lab.exception.OrderForTheCarAlreadyExists;
import com.y_lab.y_lab.service.OrderService;
import com.y_lab.y_lab.web.order.service.chain.chain.ChainFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ChainFilter chainFilter;

    @Operation(summary = "Create a new order", description = "Creates a new order for a specified car and customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Order for the car already exists", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Error creating order", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/create", produces = "application/json")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        try {
            orderService.createOrder(orderCreateRequestDto.carId(), orderCreateRequestDto.customerId());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Заказ успешно создан."));
        } catch (OrderForTheCarAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Заказ на данный автомобиль уже существует."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ошибка при создании заказа."));
        }
    }

    @Operation(summary = "Delete an order", description = "Cancels an existing order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully canceled", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid order ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error canceling order", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseEntity<?> deleteOrder(@RequestParam("id") Long orderId) {
        try {
            orderService.canceledOrder(orderId);
            return ResponseEntity.ok(Map.of("message", "Заказ успешно отменен."));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Некорректный идентификатор заказа."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ошибка при удалении заказа."));
        }
    }

    @Operation(summary = "Filter orders", description = "Filters orders based on specified parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered orders retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error retrieving orders", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/filter", produces = "application/json")
    public ResponseEntity<?> filterOrders(@RequestParam Map<String, String[]> parameterMap) {
        try {
            List<Order> filteredOrders = new ArrayList<>();

            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String filterName = entry.getKey();
                String[] arguments;
                Object value = entry.getValue();

                if (value instanceof String[]) {
                    arguments = (String[]) value;
                } else if (value instanceof String) {
                    arguments = new String[]{(String) value};
                } else {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid filter parameter."));
                }

                List<Order> result = chainFilter.assemblingTheChain(filterName, arguments);

                // Если это первый результат, добавляем его в список
                if (filteredOrders.isEmpty()) {
                    filteredOrders.addAll(result);
                } else {
                    // Оставляем только пересекающиеся элементы между фильтрами
                    filteredOrders.retainAll(result);
                }
            }

            return ResponseEntity.ok(filteredOrders);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid filter parameter."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @Operation(summary = "Find an order", description = "Finds an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Error finding order", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/find", produces = "application/json")
    public ResponseEntity<?> findOrder(@RequestParam("id") Long orderId) {
        try {
            Order order = orderService.searchOrderById(orderId);
            if (order != null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Заказ не найден."));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ошибка при поиске заказа."));
        }
    }

    @Operation(summary = "Update order status", description = "Updates the status of an existing order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid order status", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error updating order status", content = @Content(mediaType = "application/json"))
    })
    @PutMapping(value = "/update-status", produces = "application/json")
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateOrderStatusRequestDto dto) {
        try {
            orderService.changeStatus(dto.orderId(), dto.orderStatus());
            return ResponseEntity.ok(Map.of("message", "Статус заказа успешно изменен."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Некорректный статус заказа."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Ошибка при изменении статуса заказа."));
        }
    }
}
