package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.OrderDetail;
import com.dima.entity.PizzaToOrder;
import com.dima.filter.PizzaFilter;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dima.entity.QOrderDetail.orderDetail;
import static com.dima.entity.QPizza.pizza;
import static com.dima.entity.QPizzaToOrder.pizzaToOrder;

@Repository
public class OrderDetailDao extends DaoBase<Long, OrderDetail> {

    public OrderDetailDao(EntityManager entityManager) {
        super(OrderDetail.class, entityManager);
    }

    /**
     * Возвращает все заказы пицц, с указанным названием пиццы,
     * упорядоченные по количеству пицц, а затем по финальной стоимости заказа
     */
    public List<OrderDetail> findAllOrdersByPizzaName(EntityManager entityManager, PizzaFilter pizzaFilter) {
        EntityGraph<OrderDetail> orderDetailGraph = entityManager.createEntityGraph(OrderDetail.class);
        orderDetailGraph.addAttributeNodes("pizzaToOrder");
        Subgraph<PizzaToOrder> pizzaToOrderSubGraph = orderDetailGraph.addSubgraph("pizzaToOrder", PizzaToOrder.class);
        pizzaToOrderSubGraph.addAttributeNodes("pizza");

        Predicate predicate = QPredicate.builder()
                .add(pizzaFilter.getPizzaName(), pizza.name::eq)
                .buildAnd();

        return new JPAQuery<OrderDetail>(entityManager)
                .select(orderDetail)
                .from(orderDetail)
                .join(orderDetail.pizzaToOrder, pizzaToOrder)
                .join(pizzaToOrder.pizza, pizza)
                .where(predicate)
                .orderBy(pizzaToOrder.count.asc(), orderDetail.order.finalPrice.asc())
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), orderDetailGraph)
                .fetch();
    }
}
