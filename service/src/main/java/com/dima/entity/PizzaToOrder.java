package com.dima.entity;

import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"ingredientToOrders", "orderDetails"})
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "pizza_to_order")
public class PizzaToOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id",nullable = false)
    private Pizza pizza;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Enumerated(EnumType.STRING)
    private TypeDough type;

    @Column(name = "count")
    private Integer count;

    private BigDecimal price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "pizzaToOrder")
    private List<IngredientToOrder> ingredientToOrders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pizzaToOrder")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Integer getPizza() {
        return pizza.getId();
    }
}
