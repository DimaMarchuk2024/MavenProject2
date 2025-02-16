package com.dima.entity;

import com.dima.Enum.Size;
import com.dima.Enum.TypeDough;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pizza_to_order")
public class PizzaToOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pizza_id")
    private Integer pizzaId;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Enumerated(EnumType.STRING)
    private TypeDough type;

    @Column(name = "number_pizza")
    private Integer numberPizza;

    @Column(name = "added_ingredient_id")
    private Integer addedIngredientId;

    @Column(name = "removed_ingredient_id")
    private Integer removedIngredientId;

    private Double price;

    @Column(name = "users_id")
    private Long userId;
}
