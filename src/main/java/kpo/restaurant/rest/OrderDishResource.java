package kpo.restaurant.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.validation.ValidationException;
import kpo.restaurant.model.OrderDishDTO;
import kpo.restaurant.service.OrderDishService;
import kpo.restaurant.util.ErrorResponse;
import kpo.restaurant.util.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/orderDishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderDishResource {

    private final OrderDishService orderDishService;

    public OrderDishResource(final OrderDishService orderDishService) {
        this.orderDishService = orderDishService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<OrderDishDTO>> getAllOrderDishes() {
        return ResponseEntity.ok(orderDishService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<OrderDishDTO> getOrderDish(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(orderDishService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    public ResponseEntity<Integer> createOrderDish(
            @RequestBody @Valid final OrderDishDTO orderDishDTO) {
        final Integer createdId = orderDishService.create(orderDishDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    public ResponseEntity<Void> updateOrderDish(@PathVariable(name = "id") final Integer id,
                                                @RequestBody @Valid final OrderDishDTO orderDishDTO) {
        orderDishService.update(id, orderDishDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrderDish(@PathVariable(name = "id") final Integer id) {
        orderDishService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
