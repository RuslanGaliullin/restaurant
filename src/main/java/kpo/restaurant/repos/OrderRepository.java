package kpo.restaurant.repos;

import kpo.restaurant.domain.Order;
import kpo.restaurant.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findFirstByUser(User user);

    // Обновление статуса
    @Modifying
    @Query("update Order u set u.status = :status where u.id = :id")
    void updateStatus(@Param(value = "id") Integer id, @Param(value = "status") String status);
}
