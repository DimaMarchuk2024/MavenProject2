package com.dima.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "pizza_ingredient")
public class PizzaIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
        this.pizza.getPizzaIngredients().add(this);
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.ingredient.getPizzaIngredients().add(this);
    }
}
