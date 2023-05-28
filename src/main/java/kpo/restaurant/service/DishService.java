package kpo.restaurant.service;

import java.util.List;

import kpo.restaurant.domain.Dish;
import kpo.restaurant.domain.OrderDish;
import kpo.restaurant.model.DishDTO;
import kpo.restaurant.repos.DishRepository;
import kpo.restaurant.repos.OrderDishRepository;
import kpo.restaurant.util.NotFoundException;
import kpo.restaurant.util.ValidationException;
import kpo.restaurant.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DishService {

    private final DishRepository dishRepository;
    private final OrderDishRepository orderDishRepository;

    public DishService(final DishRepository dishRepository,
                       final OrderDishRepository orderDishRepository) {
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
    }

    public List<DishDTO> findAll() {
        final List<Dish> dishes = dishRepository.findAll(Sort.by("id"));
        return dishes.stream()
                .map((dish) -> mapToDTO(dish, new DishDTO()))
                .toList();
    }

    public DishDTO get(final Integer id) {
        return dishRepository.findById(id)
                .map((dish) -> mapToDTO(dish, new DishDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DishDTO dishDTO) {
        if (dishDTO.getQuantity() < 0) {
            throw new ValidationException("Quantity cannot be less then zero");
        }
        if (dishDTO.getPrice().intValue() <= 0) {
            throw new ValidationException("Price cannot be less or equal to zero");
        }
        final Dish dish = new Dish();
        mapToEntity(dishDTO, dish);
        return dishRepository.save(dish).getId();

    }

    public void update(final Integer id, final DishDTO dishDTO) {
        final Dish dish = dishRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(dishDTO, dish);
        dishRepository.save(dish);
    }

    public void delete(final Integer id) {
        dishRepository.deleteById(id);
    }

    private DishDTO mapToDTO(final Dish dish, final DishDTO dishDTO) {
        dishDTO.setId(dish.getId());
        dishDTO.setName(dish.getName());
        dishDTO.setDescription(dish.getDescription());
        dishDTO.setPrice(dish.getPrice());
        dishDTO.setQuantity(dish.getQuantity());
        return dishDTO;
    }

    private Dish mapToEntity(final DishDTO dishDTO, final Dish dish) {
        dish.setName(dishDTO.getName());
        dish.setDescription(dishDTO.getDescription());
        dish.setPrice(dishDTO.getPrice());
        dish.setQuantity(dishDTO.getQuantity());
        return dish;
    }

    public String getReferencedWarning(final Integer id) {
        final Dish dish = dishRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final OrderDish dishOrderDish = orderDishRepository.findFirstByDish(dish);
        if (dishOrderDish != null) {
            return WebUtils.getMessage("dish.orderDish.dish.referenced", dishOrderDish.getId());
        }
        return null;
    }

    public void update(final Integer id, final Dish dish) {
        Dish base_dish = dishRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mapToDTO(dish, new DishDTO()), base_dish);
        dishRepository.save(base_dish);
    }
}
