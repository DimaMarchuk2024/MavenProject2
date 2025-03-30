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
@Table(name = "ingredient_to_order")
public class IngredientToOrder implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_to_order_id")
    private PizzaToOrder pizzaToOrder;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public void setPizzaToOrder(PizzaToOrder pizzaToOrder) {
        this.pizzaToOrder = pizzaToOrder;
        this.pizzaToOrder.getIngredientToOrders().add(this);
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.ingredient.getIngredientToOrders().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientToOrder that = (IngredientToOrder) o;
        return Objects.equals(pizzaToOrder.getId(), that.pizzaToOrder.getId())
               && Objects.equals(ingredient.getId(), that.ingredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizzaToOrder.getId(), ingredient.getId());
    }

    @Override
    public String toString() {
        return "IngredientToOrder{" +
               "pizzaToOrderId=" + pizzaToOrder.getId() +
               ", ingredientId=" + ingredient.getId() +
               '}';
    }
}
