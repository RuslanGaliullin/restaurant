package kpo.restaurant.service;

import java.math.BigDecimal;
import java.util.List;

import kpo.restaurant.domain.Dish;
import kpo.restaurant.domain.Order;
import kpo.restaurant.domain.OrderDish;
import kpo.restaurant.model.OrderDishDTO;
import kpo.restaurant.repos.DishRepository;
import kpo.restaurant.repos.OrderDishRepository;
import kpo.restaurant.repos.OrderRepository;
import kpo.restaurant.util.NotFoundException;
import kpo.restaurant.util.ValidationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderDishService {

    private final OrderDishRepository orderDishRepository;
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final DishService dishService;

    public OrderDishService(final OrderDishRepository orderDishRepository,
                            final OrderRepository orderRepository, final DishRepository dishRepository, DishService dishService) {
        this.orderDishRepository = orderDishRepository;
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.dishService = dishService;
    }

    public List<OrderDishDTO> findAll() {
        final List<OrderDish> orderDishes = orderDishRepository.findAll(Sort.by("id"));
        return orderDishes.stream()
                .map((orderDish) -> mapToDTO(orderDish, new OrderDishDTO()))
                .toList();
    }

    public OrderDishDTO get(final Integer id) {
        return orderDishRepository.findById(id)
                .map((orderDish) -> mapToDTO(orderDish, new OrderDishDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OrderDishDTO orderDishDTO) {
        final OrderDish orderDish = new OrderDish();
        if (orderDishDTO.getQuantity() <= 0) {
            throw new ValidationException("Quantity cannot be less or equal to zero");
        }
        var dish = dishRepository.findById(orderDishDTO.getDish()).orElseThrow(NotFoundException::new);
        if (dish.getQuantity() < orderDishDTO.getQuantity()) {
            throw new ValidationException(String.format("Check menu, we have only %d left of this dish", dish.getQuantity()));
        }
        if (dish.getPrice() == null && dish.getPrice().intValue() * orderDishDTO.getQuantity() != orderDishDTO.getPrice().intValue()) {
            throw new ValidationException(String.format("Your price for order dish is incorrect should be %d", dish.getPrice().intValue() * orderDishDTO.getQuantity()));
        }
        dish.setQuantity(dish.getQuantity() - orderDishDTO.getQuantity());
        dishService.update(dish.getId(), dish);
        mapToEntity(orderDishDTO, orderDish);
        orderDish.setPrice(BigDecimal.valueOf((long) dish.getPrice().intValue() * orderDishDTO.getQuantity()));
        return orderDishRepository.save(orderDish).getId();
    }

    public void update(final Integer id, final OrderDishDTO orderDishDTO) {
        final OrderDish orderDish = orderDishRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if (orderDishDTO.getQuantity() <= 0) {
            throw new ValidationException("Quantity cannot be less or equal to zero");
        }
        var dish = dishRepository.findById(orderDishDTO.getDish()).orElseThrow(NotFoundException::new);
        if (dish.getQuantity() < (orderDishDTO.getQuantity() - orderDish.getQuantity())) {
            throw new ValidationException(String.format("Not enough dish to add, we have only %d left", dish.getQuantity()));
        }
        dish.setQuantity(dish.getQuantity() - orderDishDTO.getQuantity() + orderDish.getQuantity());
        dishService.update(dish.getId(), dish);
        mapToEntity(orderDishDTO, orderDish);
        orderDishRepository.save(orderDish);
    }

    public void delete(final Integer id) {
        orderDishRepository.deleteById(id);
    }

    private OrderDishDTO mapToDTO(final OrderDish orderDish, final OrderDishDTO orderDishDTO) {
        orderDishDTO.setId(orderDish.getId());
        orderDishDTO.setQuantity(orderDish.getQuantity());
        orderDishDTO.setPrice(orderDish.getPrice());
        orderDishDTO.setOrder(orderDish.getOrder() == null ? null : orderDish.getOrder().getId());
        orderDishDTO.setDish(orderDish.getDish() == null ? null : orderDish.getDish().getId());
        return orderDishDTO;
    }

    private OrderDish mapToEntity(final OrderDishDTO orderDishDTO, final OrderDish orderDish) {
        orderDish.setQuantity(orderDishDTO.getQuantity());
        orderDish.setPrice(orderDishDTO.getPrice());
        final Order order = orderDishDTO.getOrder() == null ? null : orderRepository.findById(orderDishDTO.getOrder())
                .orElseThrow(() -> new NotFoundException("order not found"));
        orderDish.setOrder(order);
        final Dish dish = orderDishDTO.getDish() == null ? null : dishRepository.findById(orderDishDTO.getDish())
                .orElseThrow(() -> new NotFoundException("dish not found"));
        orderDish.setDish(dish);
        return orderDish;
    }

    public boolean dishExists(final Integer id) {
        return orderDishRepository.existsByDishId(id);
    }

}
