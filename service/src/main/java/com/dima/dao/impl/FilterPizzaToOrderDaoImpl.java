package com.dima.dao.impl;

import com.dima.entity.OrderDetail;
import com.dima.entity.PizzaToOrder;
import com.dima.filter.PizzaFilter;
import com.dima.filter.UserFilter;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import static com.dima.entity.QOrder.order;
import static com.dima.entity.QOrderDetail.orderDetail;
import static com.dima.entity.QPizzaToOrder.pizzaToOrder;
import static com.dima.entity.QUser.user;

@RequiredArgsConstructor
public class FilterPizzaToOrderDaoImpl implements FilterPizzaToOrderDao {

    private final EntityManager entityManager;

    /**
     * Найти все заказанные пиццы пользователем по фрагменту имени, или фрагменту фамилии,
     * или до даты дня рождения, или фрагменту телефонного номера,
     * или фрагменту почты, упорядоченные по стоимости заказанной пиццы.
     */
    @Override
    public List<PizzaToOrder> findAllByUserFilter(UserFilter userFilter) {
        EntityGraph<PizzaToOrder> pizzaToOrderEntityGraph = entityManager.createEntityGraph(PizzaToOrder.class);
        pizzaToOrderEntityGraph.addAttributeNodes("user");

        Predicate predicate = QPredicate.builder()
                .add(userFilter.getFirstname(), user.firstname::containsIgnoreCase)
                .add(userFilter.getLastname(), user.lastname::containsIgnoreCase)
                .add(userFilter.getBirthDate(), user.birthDate::before)
                .add(userFilter.getPhoneNumber(), user.phoneNumber::containsIgnoreCase)
                .add(userFilter.getEmail(), user.email::containsIgnoreCase)
                .buildOr();

        return new JPAQuery<PizzaToOrder>(entityManager)
                .select(pizzaToOrder)
                .from(pizzaToOrder)
                .join(pizzaToOrder.user, user)
                .where(predicate)
                .orderBy(pizzaToOrder.price.asc())
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), pizzaToOrderEntityGraph)
                .fetch();
    }

    /**
     * Найти все заказанные пиццы по ее имени, упорядоченные по дате заказа.
     */
    @Override
    public List<PizzaToOrder> findAllByPizzaFilter(PizzaFilter pizzaFilter) {
        EntityGraph<PizzaToOrder> pizzaToOrderEntityGraph = entityManager.createEntityGraph(PizzaToOrder.class);
        pizzaToOrderEntityGraph.addAttributeNodes("orderDetails");
        Subgraph<OrderDetail> orderDetailSubgraph = pizzaToOrderEntityGraph.addSubgraph("orderDetails", OrderDetail.class);
        orderDetailSubgraph.addAttributeNodes("order");

        Predicate predicate = QPredicate.builder()
                .add(pizzaFilter.getPizzaName(), pizzaToOrder.pizza.name::containsIgnoreCase)
                .buildOr();

        return new JPAQuery<PizzaToOrder>(entityManager)
                .select(pizzaToOrder)
                .from(pizzaToOrder)
                .join(pizzaToOrder.orderDetails, orderDetail)
                .join(orderDetail.order, order)
                .where(predicate)
                .orderBy(orderDetail.order.dateTime.asc())
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), pizzaToOrderEntityGraph)
                .fetch();
    }
}
