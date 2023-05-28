package kpo.restaurant.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class OrderDish {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_order_dish_sequence",
            sequenceName = "primary_order_dish_sequence",
            allocationSize = 1,
            initialValue = 0
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_order_dish_sequence"
    )
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

}
