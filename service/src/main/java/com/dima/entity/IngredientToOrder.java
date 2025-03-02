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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "ingredient_to_order")
public class IngredientToOrder {

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
}
