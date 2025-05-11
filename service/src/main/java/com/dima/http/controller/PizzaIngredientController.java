package com.dima.http.controller;

import com.dima.dto.PageResponse;
import com.dima.dto.PizzaIngredientCreateEditDto;
import com.dima.dto.PizzaIngredientReadDto;
import com.dima.service.IngredientService;
import com.dima.service.PizzaIngredientService;
import com.dima.service.PizzaService;
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
@RequestMapping("/pizza-ingredients")
public class PizzaIngredientController {

    private final PizzaIngredientService pizzaIngredientService;
    private final PizzaService pizzaService;
    private final IngredientService ingredientService;

    @GetMapping
    public String findAll(Model model, Pageable pageable) {
        Page<PizzaIngredientReadDto> page = pizzaIngredientService.findAll(pageable);
        model.addAttribute("pizzaIngredients", PageResponse.of(page));
        model.addAttribute("pizzas", pizzaService.findAll(pageable));
        model.addAttribute("ingredients", ingredientService.findAll(pageable));

        return "pizzaIngredient/pizzaIngredients";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model, Pageable pageable) {
        return pizzaIngredientService.findById(id)
                .map(pizzaIngredientReadDto  -> {
                    model.addAttribute("pizzaIngredient", pizzaIngredientReadDto);
                    model.addAttribute("pizzas", pizzaService.findAll(pageable));
                    model.addAttribute("ingredients", ingredientService.findAll(pageable));
                    return "pizzaIngredient/pizzaIngredient";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public String create(@ModelAttribute PizzaIngredientCreateEditDto pizzaIngredientCreateEditDto) {
        pizzaIngredientService.create(pizzaIngredientCreateEditDto);

        return "redirect:/pizza-ingredients";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, @ModelAttribute PizzaIngredientCreateEditDto pizzaIngredientCreateEditDto) {
        return pizzaIngredientService.update(id, pizzaIngredientCreateEditDto)
                .map(it -> "redirect:/pizza-ingredients/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        if (!pizzaIngredientService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return "redirect:/pizza-ingredients";
    }
}

