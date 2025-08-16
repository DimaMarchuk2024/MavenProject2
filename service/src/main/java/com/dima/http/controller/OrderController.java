package com.dima.http.controller;

import com.dima.dto.OrderReadDto;
import com.dima.dto.PageResponse;
import com.dima.dto.UserReadDto;
import com.dima.service.OrderService;
import com.dima.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String findAll(Model model, Pageable pageable) {
        Page<OrderReadDto> page = orderService.findAll(pageable);
        model.addAttribute("orders", PageResponse.of(page));

        return "orders/orders";
    }

    @GetMapping("/users/{userId}")
    public String findAllByUserId(@PathVariable("userId") Long userId,
                                  Model model) {
        List<OrderReadDto> ordersByUserId = orderService.findAllByUserId(userId);
        UserReadDto user = userService.findById(userId).orElseThrow();
        model.addAttribute("ordersByUserId", ordersByUserId);
        model.addAttribute("user", user);

        return "orders/ordersByUserId";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return orderService.findById(id)
                .map(orderReadDto -> {
                    model.addAttribute("order", orderReadDto);
                    return "orders/order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

