package com.dima.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pizza_ingredient")
public class PizzaIngredient implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaIngredient that = (PizzaIngredient) o;
        return Objects.equals(pizza.getId(), that.pizza.getId())
               && Objects.equals(ingredient.getId(), that.ingredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizza.getId(), ingredient.getId());
    }

    @Override
    public String toString() {
        return "PizzaIngredient{" +
               "pizzaId=" + pizza.getId() +
               ", ingredientId=" + ingredient.getId() +
               '}';
    }
}
