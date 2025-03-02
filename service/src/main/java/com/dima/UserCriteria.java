package com.dima;

import com.dima.entity.OrderDetail;
import com.dima.entity.OrderDetail_;
import com.dima.entity.Order_;
import com.dima.entity.Pizza;
import com.dima.entity.PizzaToOrder;
import com.dima.entity.PizzaToOrder_;
import com.dima.entity.Pizza_;
import com.dima.entity.User;
import com.dima.entity.User_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaJoin;
import org.hibernate.query.criteria.JpaListJoin;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCriteria {

    private static final UserCriteria INSTANCE = new UserCriteria();

    /**
     * Возвращает всех пользователей
     */
    public List<User> findAll(Session session) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех пользователей с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstname) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user).where(cb.equal(user.get(User_.firstname),firstname));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает первых {limit} пользователей, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get(User_.birthDate)));

        return session.createQuery(criteria).setMaxResults(limit).list();
    }

    /**
     * Возвращает все пиццы для заказа (PizzaToOrder) с указанным названием
     */
    public List<PizzaToOrder> findAllPizzaToOrderByPizzaName(Session session, String pizzaName) {
        RootGraph<PizzaToOrder> pizzaToOrderGraph = session.createEntityGraph(PizzaToOrder.class);
        pizzaToOrderGraph.addAttributeNodes("pizza");

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<PizzaToOrder> criteria = cb.createQuery(PizzaToOrder.class);
        JpaRoot<Pizza> pizza = criteria.from(Pizza.class);
        JpaListJoin<Pizza, PizzaToOrder> pizzaToOrder = pizza.join(Pizza_.pizzaToOrders);

        criteria.select(pizzaToOrder)
                .where(cb.equal(pizza.get(Pizza_.name), pizzaName));

        return session.createQuery(criteria)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), pizzaToOrderGraph)
                .list();
    }

    /**
     * Возвращает все заказы пицц, с указанным названием пиццы,
     * упорядоченные по количеству пицц, а затем по финальной стоимости заказа
     */
    public List<OrderDetail> findAllOrdersByPizzaName(Session session, String pizzaName) {
        RootGraph<OrderDetail> orderDetailGraph = session.createEntityGraph(OrderDetail.class);
        orderDetailGraph.addAttributeNodes("pizzaToOrder");
        SubGraph<PizzaToOrder> pizzaToOrderSubGraph = orderDetailGraph.addSubgraph("pizzaToOrder", PizzaToOrder.class);
        pizzaToOrderSubGraph.addAttributeNodes("pizza");

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<OrderDetail> criteria = cb.createQuery(OrderDetail.class);
        JpaRoot<OrderDetail> orderDetail = criteria.from(OrderDetail.class);
        JpaJoin<OrderDetail, PizzaToOrder> pizzaToOrder = orderDetail.join(OrderDetail_.pizzaToOrder);
        JpaJoin<PizzaToOrder, Pizza> pizza = pizzaToOrder.join(PizzaToOrder_.pizza);

        criteria.select(orderDetail).where(cb.equal(pizza.get(Pizza_.name), pizzaName))
                .orderBy(
                        cb.asc(pizzaToOrder.get(PizzaToOrder_.count)),
                        cb.asc(orderDetail.get(OrderDetail_.order).get(Order_.finalPrice))
                );

        return session.createQuery(criteria)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), orderDetailGraph)
                .list();
    }

    /**
     * Возвращает среднее количество заказанных пицц пользователем с указанными именем и фамилией
     */
    public Double findAverageCountPizzaToOrderByFirstAndLastNames(Session session, String firstname, String lastname) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        JpaRoot<PizzaToOrder> pizzaToOrder = criteria.from(PizzaToOrder.class);
        JpaJoin<PizzaToOrder, User> user = pizzaToOrder.join(PizzaToOrder_.user);

        criteria.select(cb.avg(pizzaToOrder.get(PizzaToOrder_.count))).where(
                cb.equal(user.get(User_.firstname), firstname),
                cb.equal(user.get(User_.lastname), lastname)
        );

        return session.createQuery(criteria).uniqueResult();
    }

    /**
     * Возвращает для каждой пиццы: название, среднее количество в заказах. Пиццы упорядочены по названию.
     */
    public List<Object[]> findPizzaNamesWithAvgCountToOrderOrderedByPizzaName(Session session) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Object[]> criteria = cb.createQuery(Object[].class);
        JpaRoot<Pizza> pizza = criteria.from(Pizza.class);
        JpaListJoin<Pizza, PizzaToOrder> pizzaToOrder = pizza.join(Pizza_.pizzaToOrders);

        criteria.multiselect(
                pizza.get(Pizza_.name),
                cb.avg(pizzaToOrder.get(PizzaToOrder_.count))
        )
                .groupBy(pizza.get(Pizza_.name))
                .orderBy(cb.asc(pizza.get(Pizza_.name)));

        return session.createQuery(criteria)
                .list();
    }
    public static UserCriteria getInstance() {
        return INSTANCE;
    }
}
