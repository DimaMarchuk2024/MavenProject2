package com.dima.http.controller;

import com.dima.dto.DeliveryAddressCreateEditDto;
import com.dima.dto.DeliveryAddressReadDto;
import com.dima.dto.PageResponse;
import com.dima.service.DeliveryAddressService;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/delivery-addresses")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @GetMapping
    public String findAllBy(Model model, String address, Pageable pageable) {
        Page<DeliveryAddressReadDto> page = deliveryAddressService.findAllBy(address, pageable);
        model.addAttribute("deliveryAddresses", PageResponse.of(page));

        return "deliveryAddress/deliveryAddresses";
    }

    @GetMapping("/user/{userId}")
    public String findAllByUserId(@PathVariable("userId") Long userId,
                                  Model model,
                                  DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        List<DeliveryAddressReadDto> addressesByUserId = deliveryAddressService.findAllByUserId(userId);
        model.addAttribute("deliveryAddressesByUserId", addressesByUserId);
        model.addAttribute("deliveryAddressCreateEditDto", deliveryAddressCreateEditDto);

        return "deliveryAddress/deliveryAddressesByUserId";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return deliveryAddressService.findById(id)
                .map(addressReadDto -> {
                    model.addAttribute("addressReadDto", addressReadDto);
                    return "deliveryAddress/deliveryAddress";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user/{userId}/create")
    public String create(@ModelAttribute DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        deliveryAddressService.create(deliveryAddressCreateEditDto);

        return "redirect:/delivery-addresses/user/{userId}";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute DeliveryAddressCreateEditDto deliveryAddressCreateEditDto) {
        return deliveryAddressService.update(id, deliveryAddressCreateEditDto)
                .map(it -> "redirect:/delivery-addresses/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        Long userId = deliveryAddressService.findById(id)
                .map(addressReadDto -> addressReadDto.getUser().getId()).orElseThrow();
        if (!deliveryAddressService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return "redirect:/delivery-addresses/user/" + userId;
    }
}

