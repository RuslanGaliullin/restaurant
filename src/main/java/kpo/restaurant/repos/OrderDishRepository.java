package kpo.restaurant.repos;

import kpo.restaurant.domain.Dish;
import kpo.restaurant.domain.Order;
import kpo.restaurant.domain.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderDishRepository extends JpaRepository<OrderDish, Integer> {

    OrderDish findFirstByDish(Dish dish);

    OrderDish findFirstByOrder(Order order);

    boolean existsByDishId(Integer id);

}
