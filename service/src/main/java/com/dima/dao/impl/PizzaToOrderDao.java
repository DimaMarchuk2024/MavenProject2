package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.PizzaToOrder;
import com.dima.filter.PizzaFilter;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dima.entity.QPizza.pizza;
import static com.dima.entity.QPizzaToOrder.pizzaToOrder;

@Repository
public class PizzaToOrderDao extends DaoBase<Long, PizzaToOrder> {

    public PizzaToOrderDao(EntityManager entityManager) {
        super(PizzaToOrder.class, entityManager);
    }

    /**
     * Возвращает все пиццы для заказа (PizzaToOrder) с указанным названием
     */
    public List<PizzaToOrder> findAllPizzaToOrderByFilter(EntityManager entityManager, PizzaFilter pizzaFilter) {
        EntityGraph<PizzaToOrder> pizzaToOrderGraph = entityManager.createEntityGraph(PizzaToOrder.class);
        pizzaToOrderGraph.addAttributeNodes("pizza");

        Predicate predicate = QPredicate.builder()
                .add(pizzaFilter.getPizzaName(), pizza.name::eq)
                .buildAnd();

        return new JPAQuery<PizzaToOrder>(entityManager)
                .select(pizzaToOrder)
                .from(pizzaToOrder)
                .join(pizzaToOrder.pizza, pizza)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), pizzaToOrderGraph)
                .fetch();
    }
}
