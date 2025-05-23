package com.dima.service;

import com.dima.dao.impl.UserDao;
import com.dima.dto.UserCreateEditDto;
import com.dima.dto.UserReadDto;
import com.dima.filter.UserFilter;
import com.dima.mapper.UserCreateEditMapper;
import com.dima.mapper.UserReadMapper;
import com.dima.predicate.QPredicate;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dima.entity.QUser.user;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public Page<UserReadDto> findAll(UserFilter userFilter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(userFilter.getFirstname(), user.firstname::containsIgnoreCase)
                .add(userFilter.getLastname(), user.lastname::containsIgnoreCase)
                .add(userFilter.getBirthDate(), user.birthDate::before)
                .add(userFilter.getPhoneNumber(), user.phoneNumber::containsIgnoreCase)
                .add(userFilter.getEmail(), user.email::containsIgnoreCase)
                .buildAnd();

       return userDao.findAll(predicate, pageable)
                .map(userReadMapper::map);
    }

    public List<UserReadDto> findAll(Pageable pageable) {
        return userDao.findAll(pageable)
                .stream()
                .map(userReadMapper::map)
                .toList();
    }

    public Optional<UserReadDto> findById(Long id) {
        return userDao.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userCreateEditDto) {
        return Optional.of(userCreateEditDto)
                .map(userCreateEditMapper::map)
                .map(userDao::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto userCreateEditDto) {
        return userDao.findById(id)
                .map(user -> userCreateEditMapper.map(userCreateEditDto, user))
                .map(userDao::saveAndFlush)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userDao.findById(id)
                .map(user -> {
                    userDao.delete(user);
                    userDao.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }
}
