package com.dima.http.controller;

import com.dima.dto.IngredientCreateEditDto;
import com.dima.dto.IngredientReadDto;
import com.dima.dto.PageResponse;
import com.dima.service.IngredientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public String findAll(Model model, Pageable pageable, IngredientCreateEditDto ingredientCreateEditDto) {
        Page<IngredientReadDto> page = ingredientService.findAll(pageable);
        model.addAttribute("ingredients", PageResponse.of(page));
        model.addAttribute("ingredientCreateEditDto", ingredientCreateEditDto);

        return "ingredient/ingredients";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Integer id, Model model) {
        return ingredientService.findById(id)
                .map(ingredientReadDto  -> {
                    model.addAttribute("ingredient", ingredientReadDto);
                    return "ingredient/ingredient";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Validated IngredientCreateEditDto ingredientCreateEditDto,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("ingredientCreateEditDto", ingredientCreateEditDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/ingredients";
        }
        ingredientService.create(ingredientCreateEditDto);
        return "redirect:/ingredients";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, @ModelAttribute @Validated IngredientCreateEditDto ingredientCreateEditDto) {
        return ingredientService.update(id, ingredientCreateEditDto)
                .map(it -> "redirect:/ingredients/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        if (!ingredientService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/ingredients";
    }
}
