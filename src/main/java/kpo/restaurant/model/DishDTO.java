package kpo.restaurant.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class DishDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer quantity;

}
