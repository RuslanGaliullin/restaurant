package kpo.restaurant.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.validation.ValidationException;
import kpo.restaurant.model.DishDTO;
import kpo.restaurant.service.DishService;
import kpo.restaurant.util.ErrorResponse;
import kpo.restaurant.util.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class DishResource {

    private final DishService dishService;

    public DishResource(final DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        return ResponseEntity.ok(dishService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<DishDTO> getDish(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(dishService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createDish(@RequestBody @Valid final DishDTO dishDTO) {
        final Integer createdId = dishService.create(dishDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> updateDish(@PathVariable(name = "id") final Integer id,
                                           @RequestBody @Valid final DishDTO dishDTO) {
        dishService.update(id, dishDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDish(@PathVariable(name = "id") final Integer id) {
        dishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
