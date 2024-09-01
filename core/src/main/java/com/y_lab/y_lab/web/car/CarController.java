package com.y_lab.y_lab.web.car;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.dto.response.CarResponseDto;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.mapper.CarMapper;
import com.y_lab.y_lab.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarMapper carMapper;
    private final CarService carService;

    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody Car car) {
        try {
            carService.addCar(car);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{carId}")
    public ResponseEntity<Void> editCar(@PathVariable Long carId, @RequestBody Car car) {
        try {
            carService.editCar(carId, car);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CarNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<CarResponseDto> deleteCar(@PathVariable Long carId) {
        try {
            Car car = carService.removeCar(carId);
            CarResponseDto carDto = carMapper.toCarResponseDto(car);
            return ResponseEntity.status(HttpStatus.OK).body(carDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        try {
            List<Car> cars = carService.getAllCars();
            List<CarResponseDto> carResponseDtos = carMapper.toCarResponseDtoList(cars);
            return ResponseEntity.status(HttpStatus.OK).body(carResponseDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
