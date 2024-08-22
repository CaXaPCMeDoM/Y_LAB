package com.y_lab.y_lab.web.car;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.dto.response.CarResponseDto;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.mapper.CarMapper;
import com.y_lab.y_lab.service.CarService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/cars")
public class CarServlet extends HttpServlet {
    private final CarMapper carMapper;
    private final ObjectMapper objectMapper;
    private final CarService carService;

    public CarServlet() {
        objectMapper = new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true);
        carService = ServiceContainer.getCarService();
        carMapper = CarMapper.INSTANCE;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Car car = objectMapper.readValue(req.getInputStream(), Car.class);

            carService.addCar(car);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Car car = objectMapper.readValue(req.getInputStream(), Car.class);

            carService.editCar(car.getCarId(), car);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (CarNotFound e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Long carId = Long.parseLong(req.getParameter("car_id"));

            Car carRemovable = carService.removeCar(carId);

            CarResponseDto carDto = carMapper.toCarResponseDto(carRemovable);

            String jsonResponse = objectMapper.writeValueAsString(carDto);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");

            resp.getWriter().write(jsonResponse);
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<Car> cars = carService.getAllCars();
            List<CarResponseDto> carResponseDtos = carMapper.toCarResponseDtoList(cars);

            String jsonResponse = objectMapper.writeValueAsString(carResponseDtos);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);

            resp.getWriter().write(jsonResponse);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
