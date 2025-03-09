package com.dima;

import com.dima.filter.PizzaFilter;
import com.dima.filter.UserFilter;
import com.dima.entity.OrderDetail;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.User;
import com.dima.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;

import static com.dima.entity.QOrderDetail.orderDetail;
import static com.dima.entity.QPizza.pizza;
import static com.dima.entity.QPizzaToOrder.pizzaToOrder;
import static com.dima.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQueryDsl {

    private static final UserQueryDsl INSTANCE = new UserQueryDsl();

    public static UserQueryDsl getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает всех пользователей
     */
    public List<User> findAll(Session session) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    /**
     * Возвращает всех пользователей с указанным именем
     */
    public List<User> findAllByFirstName(Session session, UserFilter userFilter) {
        Predicate predicate = QPredicate.builder()
                .add(userFilter.getFirstname(), user.firstname::eq)
                .buildAnd();

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }

    /**
     * Возвращает первых {limit} пользователей, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .orderBy(user.birthDate.asc())
                .limit(limit)
                .fetch();
    }

    /**
     * Возвращает все пиццы для заказа (PizzaToOrder) с указанным названием
     */
    public List<PizzaToOrder> findAllPizzaToOrderByPizzaName(Session session, PizzaFilter pizzaFilter) {
        RootGraph<PizzaToOrder> pizzaToOrderGraph = session.createEntityGraph(PizzaToOrder.class);
        pizzaToOrderGraph.addAttributeNodes("pizza");

        Predicate predicate = QPredicate.builder()
                .add(pizzaFilter.getPizzaName(), pizza.name::eq)
                .buildAnd();

        return new JPAQuery<PizzaToOrder>(session)
                .select(pizzaToOrder)
                .from(pizzaToOrder)
                .join(pizzaToOrder.pizza, pizza)
                .where(predicate)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), pizzaToOrderGraph)
                .fetch();
    }

    /**
     * Возвращает все заказы пицц, с указанным названием пиццы,
     * упорядоченные по количеству пицц, а затем по финальной стоимости заказа
     */
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

    /**
     * Возвращает среднее количество заказанных пицц пользователем с указанными именем и фамилией
     */
    public Double findAverageCountPizzaToOrderByFirstAndLastNames(Session session, UserFilter userFilter) {
        Predicate predicate = QPredicate.builder()
                .add(userFilter.getFirstname(), user.firstname::eq)
                .add(userFilter.getLastname(), user.lastname::eq)
                .buildAnd();

        return new JPAQuery<Double>(session)
                .select(pizzaToOrder.count.avg())
                .from(pizzaToOrder)
                .join(pizzaToOrder.user, user)
                .where(predicate)
                .fetchOne();
    }

    /**
     * Возвращает для каждой пиццы: название, среднее количество в заказах. Пиццы упорядочены по названию.
     */
    public List<Tuple> findPizzaNamesWithAvgCountToOrderOrderedByPizzaName(Session session) {
        return new JPAQuery<Tuple>(session)
                .select(pizza.name, pizzaToOrder.count.avg())
                .from(pizza)
                .join(pizza.pizzaToOrders, pizzaToOrder)
                .groupBy(pizza.name)
                .orderBy(pizza.name.asc())
                .fetch();
    }
}
