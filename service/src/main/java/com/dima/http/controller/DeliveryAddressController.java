package com.dima.http.controller;

import com.dima.dto.DeliveryAddressCreateEditDto;
import com.dima.dto.DeliveryAddressReadDto;
import com.dima.dto.PageResponse;
import com.dima.dto.UserReadDto;
import com.dima.service.DeliveryAddressService;
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
@RequestMapping("/deliveryAddresses")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;
    private final UserService userService;

    @GetMapping
    public String findAllBy(Model model, String address, Pageable pageable) {
        Page<DeliveryAddressReadDto> page = deliveryAddressService.findAllBy(address, pageable);
        model.addAttribute("deliveryAddresses", PageResponse.of(page));
        model.addAttribute("users", userService.findAll());

        return "deliveryAddress/deliveryAddresses";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model, UserReadDto userReadDto) {
        return deliveryAddressService.findById(id)
                .map(addressReadDto -> {
                    model.addAttribute("user", userReadDto);
                    model.addAttribute("address", addressReadDto);
                    return "deliveryAddress/deliveryAddress";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public String create(@ModelAttribute DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        return "redirect:/deliveryAddresses/" + deliveryAddressService.create(deliveryAddressCreateEditDto).getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        return deliveryAddressService.update(id, deliveryAddressCreateEditDto)
                .map(it -> "redirect:/deliveryAddresses/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!deliveryAddressService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/deliveryAddresses";
    }
}

