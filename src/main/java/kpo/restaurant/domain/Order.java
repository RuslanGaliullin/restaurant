package kpo.restaurant.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "\"order\"")
@Getter
@Setter
public class Order {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_order_sequence",
            sequenceName = "primary_order_sequence",
            allocationSize = 1,
            initialValue = 0
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_order_sequence"
    )
    private Integer id;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "text")
    private String specialRequest;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDish> orderDishes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
