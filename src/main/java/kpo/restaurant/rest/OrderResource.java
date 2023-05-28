package kpo.restaurant.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

import kpo.restaurant.model.OrderDTO;
import kpo.restaurant.service.OrderService;
import kpo.restaurant.util.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    public ResponseEntity<Integer> createOrder(@RequestBody @Valid final OrderDTO orderDTO) {
        final Integer createdId = orderService.create(orderDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    public ResponseEntity<Void> updateOrder(@PathVariable(name = "id") final Integer id,
                                            @RequestBody @Valid final OrderDTO orderDTO) {
        orderService.update(id, orderDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cook")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> cook() throws InterruptedException {
        orderService.cook();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id") final Integer id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
