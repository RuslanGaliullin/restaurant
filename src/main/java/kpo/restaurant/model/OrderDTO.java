package kpo.restaurant.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

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

}
