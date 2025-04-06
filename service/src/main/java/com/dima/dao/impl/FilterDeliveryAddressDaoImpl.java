package com.dima.dao.impl;

import com.dima.entity.DeliveryAddress;
import com.dima.filter.UserFilter;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import static com.dima.entity.QDeliveryAddress.deliveryAddress;
import static com.dima.entity.QUser.user;

@RequiredArgsConstructor
public class FilterDeliveryAddressDaoImpl implements FilterDeliveryAddressDao{

    private final EntityManager entityManager;

    /**
     Найти все адреса доставки и соответствующих пользователей
     по фрагменту телефону или фргменту email пользователя,
     упорядоченные по названию адресов доставки.
     */
    @Override
    public List<DeliveryAddress> findAllByUserFilter(UserFilter userFilter) {
        EntityGraph<DeliveryAddress> deliveryAddressEntityGraph = entityManager.createEntityGraph(DeliveryAddress.class);
        deliveryAddressEntityGraph.addAttributeNodes("user");

        Predicate predicate = QPredicate.builder()
                .add(userFilter.getPhoneNumber(), user.phoneNumber::containsIgnoreCase)
                .add(userFilter.getEmail(), user.email::containsIgnoreCase)
                .buildOr();

        return new JPAQuery<DeliveryAddress>(entityManager)
                .select(deliveryAddress)
                .from(deliveryAddress)
                .join(deliveryAddress.user, user)
                .where(predicate)
                .orderBy(deliveryAddress.address.asc())
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), deliveryAddressEntityGraph)
                .fetch();
    }
}
