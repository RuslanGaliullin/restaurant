package kpo.restaurant.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kpo.restaurant.model.DishDTO;
import kpo.restaurant.model.OrderDTO;
import kpo.restaurant.service.MenuService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuResource {

    private final MenuService menuService;

    public MenuResource(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<DishDTO>> getAvailable() {
        return ResponseEntity.ok(menuService.getAvailable().getDishes());
    }
}