package com.dima.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"pizzaIngredients", "pizzaToOrders"})
@Builder
@Entity
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "pizza")
    private List<PizzaIngredient> pizzaIngredients = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pizza")
    private List<PizzaToOrder> pizzaToOrders = new ArrayList<>();
}
