package com.dima.http.controller;

import com.dima.Enum.Role;
import com.dima.dto.PageResponse;
import com.dima.dto.UserCreateEditDto;
import com.dima.dto.UserReadDto;
import com.dima.filter.UserFilter;
import com.dima.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String findAll(Model model, UserFilter userFilter, Pageable pageable) {
        Page<UserReadDto> page = userService.findAll(userFilter, pageable);
        model.addAttribute("users", PageResponse.of(page));
        model.addAttribute("filter", userFilter);

        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model, UserCreateEditDto userCreateEditDto) {
        return userService.findById(id)
                .map(userReadDto -> {
                    model.addAttribute("user", userReadDto);
                    model.addAttribute("roles", Role.values());
                    model.addAttribute("userCreate", userCreateEditDto);
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String registration(Model model, UserCreateEditDto userCreateEditDto) {
        model.addAttribute("userCreate", userCreateEditDto);
        model.addAttribute("roles", Role.values());

        return "user/registration";
    }

    @PostMapping
    public String create(@ModelAttribute UserCreateEditDto userCreateEditDto) {
        return "redirect:/users/" + userService.create(userCreateEditDto).getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute UserCreateEditDto userCreateEditDto) {
        return userService.update(id, userCreateEditDto)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return "redirect:/users";
    }
}
