package kpo.restaurant.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kpo.restaurant.domain.Order;
import kpo.restaurant.domain.OrderDish;
import kpo.restaurant.domain.User;
import kpo.restaurant.model.OrderDTO;
import kpo.restaurant.repos.OrderDishRepository;
import kpo.restaurant.repos.OrderRepository;
import kpo.restaurant.repos.UserRepository;
import kpo.restaurant.util.NotFoundException;
import kpo.restaurant.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDishRepository orderDishRepository;

    public OrderService(final OrderRepository orderRepository, final UserRepository userRepository,
                        final OrderDishRepository orderDishRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDishRepository = orderDishRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map((order) -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Integer id) {
        return orderRepository.findById(id)
                .map((order) -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OrderDTO orderDTO) {
        orderDTO.setStatus("в ожидании");
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Integer id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Integer id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setSpecialRequest(order.getSpecialRequest());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        orderDTO.setUser(order.getUser() == null ? null : order.getUser().getId());
        orderDTO.setOrderDishes(order.getOrderDishes() == null ? null : order.getOrderDishes().stream().map(OrderDish::getId).toList());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setStatus(orderDTO.getStatus());
        order.setSpecialRequest(orderDTO.getSpecialRequest());
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setUpdatedAt(orderDTO.getUpdatedAt());
        final User user = orderDTO.getUser() == null ? null : userRepository.findById(orderDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        order.setOrderDishes(orderDTO.getOrderDishes() == null ? null : orderDTO.getOrderDishes()
                .stream()
                .map(id -> orderDishRepository.findById(id).orElseThrow(() -> new NotFoundException("order dish cannot be found")))
                .collect(Collectors.toSet()));
        order.setUser(user);
        return order;
    }

    public String getReferencedWarning(final Integer id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final OrderDish orderOrderDish = orderDishRepository.findFirstByOrder(order);
        if (orderOrderDish != null) {
            return WebUtils.getMessage("order.orderDish.order.referenced", orderOrderDish.getId());
        }
        return null;
    }

    public void cook() throws InterruptedException {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"))
                .stream()
                .filter(x -> Objects.equals(x.getStatus(), "в ожидании")).toList();
        OrderDTO orderDTO = new OrderDTO();
        for (var order : orders) {
            order.setStatus("в работе");
            update(order.getId(), mapToDTO(order, orderDTO));
        }
        Thread.sleep(6000);
        for (var order : orders) {
            order.setStatus("завершен");
            update(order.getId(), mapToDTO(order, orderDTO));
        }
    }
}
