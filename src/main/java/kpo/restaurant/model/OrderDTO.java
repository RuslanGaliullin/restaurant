package kpo.restaurant.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Integer id;

    private String status;

    private String specialRequest;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull
    private Integer user;

    private List<Integer> orderDishes;
}
