package kpo.restaurant.repos;

import kpo.restaurant.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DishRepository extends JpaRepository<Dish, Integer> {
}
