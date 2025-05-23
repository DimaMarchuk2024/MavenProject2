package com.dima.http.controller;

import com.dima.dto.PageResponse;
import com.dima.dto.PizzaCreateEditDto;
import com.dima.dto.PizzaReadDto;
import com.dima.dto.UserReadDto;
import com.dima.service.PizzaService;
import com.dima.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;
    private final UserService userService;

    @GetMapping
    public String findAll(Model model, Pageable pageable, PizzaCreateEditDto pizzaCreateEditDto,
                          @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserReadDto userReadDto = userService.findAll(pageable)
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny()
                .orElseThrow();

        Page<PizzaReadDto> page = pizzaService.findAll(pageable);
        model.addAttribute("userReadDto", userReadDto);
        model.addAttribute("pizzas", PageResponse.of(page));
        model.addAttribute("pizzaCreateEditDto", pizzaCreateEditDto);

        return "pizza/pizzas";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        return pizzaService.findById(id)
                .map(pizzaReadDto -> {
                    model.addAttribute("pizza", pizzaReadDto);
                    return "pizza/pizza";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Validated PizzaCreateEditDto pizzaCreateEditDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("pizzaCreateEditDto", pizzaCreateEditDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/pizzas";
        }
        pizzaService.create(pizzaCreateEditDto);
        return "redirect:/pizzas";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, @ModelAttribute @Validated PizzaCreateEditDto pizzaCreateEditDto) {
        return pizzaService.update(id, pizzaCreateEditDto)
                .map(it -> "redirect:/pizzas/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        if (!pizzaService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/pizzas";
    }
}
