package kpo.restaurant.service;

import kpo.restaurant.domain.Dish;
import kpo.restaurant.model.DishDTO;
import kpo.restaurant.model.Menu;
import kpo.restaurant.repos.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class MenuService {
    private final DishRepository dishRepository;

    public MenuService(final DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Menu getAvailable() {
        List<DishDTO> dishes = dishRepository.findAll().stream()
                .filter(Dish::isAvailable)
                .map(dish -> mapToDTO(dish, new DishDTO())).toList();
        var menu = new Menu();
        menu.setDishes(dishes);
        return menu;
    }


    private DishDTO mapToDTO(final Dish dish,
                             final DishDTO dishDTO) {
        dishDTO.setId(dish.getId());
        dishDTO.setName(dish.getName());
        dishDTO.setPrice(dish.getPrice());
        dishDTO.setQuantity(dish.getQuantity());
        dishDTO.setDescription(dish.getDescription() == null ? null : dish.getDescription());
        return dishDTO;
    }

}
