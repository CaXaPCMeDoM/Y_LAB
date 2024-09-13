package com.y_lab.y_lab.web.car;

import com.y_lab.y_lab.entity.Car;
import com.y_lab.y_lab.entity.dto.response.CarResponseDto;
import com.y_lab.y_lab.exception.CarNotFound;
import com.y_lab.y_lab.mapper.CarMapper;
import com.y_lab.y_lab.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a new car", description = "Adds a new car to the inventory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<Void> addCar(@RequestBody Car car) {
        try {
            carService.addCar(car);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Edit an existing car", description = "Edits an existing car's details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car edited successfully"),
            @ApiResponse(responseCode = "404", description = "Car not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PutMapping("/{carId}")
    public ResponseEntity<Void> editCar(@PathVariable Long carId, @RequestBody Car car) {
        try {
            carService.editCar(carId, car);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CarNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a car", description = "Deletes a car by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{carId}")
    public ResponseEntity<CarResponseDto> deleteCar(@PathVariable Long carId) {
        try {
            Car car = carService.removeCar(carId);
            CarResponseDto carDto = carMapper.toCarResponseDto(car);
            return new ResponseEntity<>(carDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all cars", description = "Retrieves a list of all cars in the inventory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cars retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        try {
            List<Car> cars = carService.getAllCars();
            List<CarResponseDto> carResponseDtos = carMapper.toCarResponseDtoList(cars);
            return new ResponseEntity<>(carResponseDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}