package kpo.restaurant.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import kpo.restaurant.model.OrderDishDTO;
import kpo.restaurant.service.OrderDishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/orderDishs", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderDishResource {

    private final OrderDishService orderDishService;

    public OrderDishResource(final OrderDishService orderDishService) {
        this.orderDishService = orderDishService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDishDTO>> getAllOrderDishs() {
        return ResponseEntity.ok(orderDishService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDishDTO> getOrderDish(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(orderDishService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createOrderDish(
            @RequestBody @Valid final OrderDishDTO orderDishDTO) {
        final Integer createdId = orderDishService.create(orderDishDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
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
