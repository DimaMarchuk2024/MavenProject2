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
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pizza_to_order")
public class PizzaToOrder implements BaseEntity<Long>{

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaToOrder that = (PizzaToOrder) o;
        return Objects.equals(pizza.getId(), that.pizza.getId())
               && size == that.size
               && type == that.type
               && Objects.equals(count, that.count)
               && Objects.equals(price, that.price)
               && Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pizza.getId(), size, type, count, price, user.getId());
    }

    @Override
    public String toString() {
        return "PizzaToOrder{" +
               "pizzaId=" + pizza.getId() +
               ", size=" + size +
               ", type=" + type +
               ", count=" + count +
               ", price=" + price +
               ", userId=" + user.getId() +
               '}';
    }

    @Override
    public Long get() {
        return id;
    }
}
