package com.dima.http.controller;

import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import com.dima.dto.PizzaIngredientReadDto;
import com.dima.dto.PizzaReadDto;
import com.dima.dto.PizzaToOrderCreateEditDto;
import com.dima.service.IngredientService;
import com.dima.service.PizzaIngredientService;
import com.dima.service.PizzaService;
import com.dima.service.PizzaToOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping()
public class PizzaToOrderController {

    private final IngredientService ingredientService;
    private final PizzaService pizzaService;
    private final PizzaIngredientService pizzaIngredientService;

    @GetMapping("/pizzas/{id}/to-bucket")
    public String toBucket(@PathVariable("id") Integer id, Model model, Pageable pageable, PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto) {
        PizzaReadDto pizzaReadDto = pizzaService.findById(id).orElseThrow();
        List<PizzaIngredientReadDto> pizzaIngredientReadDtos = pizzaIngredientService.findAllByPizzaId(id);
        model.addAttribute("pizzaIngredients", pizzaIngredientReadDtos);
        model.addAttribute("pizza", pizzaReadDto);
        model.addAttribute("pizzaToOrderCreateEditDto", pizzaToOrderCreateEditDto);
        model.addAttribute("sizes", Size.values());
        model.addAttribute("types", TypeDough.values());
        model.addAttribute("ingredients", ingredientService.findAll(pageable));

        return "pizzaToOrder/toBucket";
    }
}
