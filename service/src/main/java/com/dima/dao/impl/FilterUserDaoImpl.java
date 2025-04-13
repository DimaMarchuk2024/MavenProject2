package com.dima.dao.impl;

import com.dima.entity.User;
import com.dima.filter.UserFilter;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dima.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserDaoImpl implements FilterUserDao {

    private final EntityManager entityManager;

    /**
     * Найти всех пользователей по фрагменту имени, или фрагменту фамилии,
     * или до даты дня рождения, или фрагменту телефонного номера,
     * или фрагменту почты, упорядоченные сначала по имени, а потом по фамилии.
     */
    @Override
    public List<User> findAllByFilter(UserFilter userFilter) {
        Predicate predicate = QPredicate.builder()
                .add(userFilter.getFirstname(), user.firstname::containsIgnoreCase)
                .add(userFilter.getLastname(), user.lastname::containsIgnoreCase)
                .add(userFilter.getBirthDate(), user.birthDate::before)
                .add(userFilter.getPhoneNumber(), user.phoneNumber::containsIgnoreCase)
                .add(userFilter.getEmail(), user.email::containsIgnoreCase)
                .buildAnd();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.firstname.asc(), user.lastname.asc())
                .fetch();
    }
}
