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

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_detail")
public class OrderDetail implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_to_order_id")
    private PizzaToOrder pizzaToOrder;

    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(order.getId(), that.order.getId())
               && Objects.equals(pizzaToOrder.getId(), that.pizzaToOrder.getId())
               && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order.getId(), pizzaToOrder.getId(), price);
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
               "orderId=" + order.getId() +
               ", pizzaToOrderId=" + pizzaToOrder.getId() +
               ", price=" + price +
               '}';
    }
}
