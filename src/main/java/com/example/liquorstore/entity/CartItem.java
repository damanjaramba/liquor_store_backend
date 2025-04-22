package com.example.liquorstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToMany
    @JoinTable(
            name = "cart_item_liquors",
            joinColumns = @JoinColumn(name = "cart_item_id"),
            inverseJoinColumns = @JoinColumn(name = "liquor_id")
    )
    private List<Liquor> liquors;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public void updateTotalPrice() {
        this.totalPrice = liquors.stream()
                .map(Liquor::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(BigDecimal.valueOf(quantity));
    }

}
