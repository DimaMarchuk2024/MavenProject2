package com.dima.dao.impl;

import com.dima.dao.DaoBase;
import com.dima.entity.OrderDetail;
import com.dima.entity.PizzaToOrder;
import com.dima.filter.PizzaFilter;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;

import static com.dima.entity.QOrderDetail.orderDetail;
import static com.dima.entity.QPizza.pizza;
import static com.dima.entity.QPizzaToOrder.pizzaToOrder;

public class OrderDetailDao extends DaoBase<Long, OrderDetail> {

    public OrderDetailDao(Session session) {
        super(OrderDetail.class, session);
    }

    public List<OrderDetail> findAllOrdersByPizzaName(Session session, PizzaFilter pizzaFilter) {
        RootGraph<OrderDetail> orderDetailGraph = session.createEntityGraph(OrderDetail.class);
        orderDetailGraph.addAttributeNodes("pizzaToOrder");
        SubGraph<PizzaToOrder> pizzaToOrderSubGraph = orderDetailGraph.addSubgraph("pizzaToOrder", PizzaToOrder.class);
        pizzaToOrderSubGraph.addAttributeNodes("pizza");

        Predicate predicate = QPredicate.builder()
                .add(pizzaFilter.getPizzaName(), pizza.name::eq)
                .buildAnd();

        return new JPAQuery<OrderDetail>(session)
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
