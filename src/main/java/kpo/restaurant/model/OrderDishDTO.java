package kpo.restaurant.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDishDTO {

    private Integer id;

    @NotNull
    private Integer quantity;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Integer order;

    @NotNull
    private Integer dish;

}
