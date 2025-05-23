package com.dima.http.controller;

import com.dima.dao.impl.OrderDao;
import com.dima.dto.DeliveryAddressReadDto;
import com.dima.dto.PizzaToOrderInBucket;
import com.dima.dto.UserReadDto;
import com.dima.entity.Order;
import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import com.dima.dto.PizzaIngredientReadDto;
import com.dima.dto.PizzaReadDto;
import com.dima.dto.PizzaToOrderCreateEditDto;
import com.dima.service.DeliveryAddressService;
import com.dima.service.IngredientService;
import com.dima.service.PizzaIngredientService;
import com.dima.service.PizzaService;
import com.dima.service.PizzaToOrderService;
import com.dima.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes("bucket")
@RequestMapping()
public class PizzaToOrderController {

    private final IngredientService ingredientService;
    private final PizzaService pizzaService;
    private final PizzaIngredientService pizzaIngredientService;
    private final PizzaToOrderService pizzaToOrderService;
    private final UserService userService;
    private final DeliveryAddressService deliveryAddressService;
    private final OrderDao orderDao;

    @GetMapping("/pizzas/{id}/to-bucket")
    public String toBucket(@PathVariable("id") Integer id,
                           Model model,
                           Pageable pageable,
                           PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto,
                           @AuthenticationPrincipal UserDetails userDetails) {
        PizzaReadDto pizzaReadDto = pizzaService.findById(id).orElseThrow();

        List<PizzaIngredientReadDto> pizzaIngredientReadDtos = pizzaIngredientService.findAllByPizzaId(id);

        List<Integer> pizzaIngredientsIds = pizzaIngredientReadDtos.stream().map(it -> it.getIngredientReadDto().getId()).toList();

        String email = userDetails.getUsername();
        UserReadDto userReadDto = userService.findAll(pageable)
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny()
                .orElseThrow();

        model.addAttribute("userReadDto", userReadDto);
        model.addAttribute("pizzaIngredientsIds", pizzaIngredientsIds);
        model.addAttribute("pizza", pizzaReadDto);
        model.addAttribute("pizzaToOrderCreateEditDto", pizzaToOrderCreateEditDto);
        model.addAttribute("sizes", Size.values());
        model.addAttribute("types", TypeDough.values());
        model.addAttribute("ingredients", ingredientService.findAll(pageable));

        return "pizzaToOrder/toBucket";
    }

    @GetMapping("/bucket")
    public String bucketView(Model model,
                             @ModelAttribute PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto,
                             Bucket bucket) {
        model.addAttribute("bucket", bucket);

        return "pizzaToOrder/bucket";
    }

    @PostMapping("/bucket/{index}/remove")
    public String bucketRemove(@PathVariable("index") int index,
                               Bucket bucket) {
        bucket.getPizzas().remove(index);

        return "redirect:/bucket";
    }

    @PostMapping("/bucket/add")
    public String bucketAdd(@ModelAttribute @Validated PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes,
                            Bucket bucket) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("pizzaToOrderCreateEditDto", pizzaToOrderCreateEditDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/pizzas/" + pizzaToOrderCreateEditDto.getPizzaId() + "/to-bucket" ;
        }
        var pizzaToOrderInBucket = pizzaToOrderService.prepareToCheckout(pizzaToOrderCreateEditDto);
        bucket.getPizzas().add(pizzaToOrderInBucket);

        return "redirect:/bucket";
    }

    @GetMapping("/checkout")
    public String checkoutView(Model model,
                               Bucket bucket) {
        model.addAttribute("bucket", bucket);
        Long userId = bucket.getPizzas().stream()
                .map(PizzaToOrderInBucket::getUserId)
                .findAny()
                .orElseThrow();
        List<DeliveryAddressReadDto> addresses = deliveryAddressService.findAllByUserId(userId);
        model.addAttribute("addresses", addresses);
        model.addAttribute("userId", userId);

        return "pizzaToOrder/checkout";
    }

    @PostMapping("/order/create")
    public String finishAdd( Model model,
                            Bucket bucket) {
        model.addAttribute("bucket", bucket);
        Order order = pizzaToOrderService.create(bucket);
        model.addAttribute("order", order);
        bucket.getPizzas().clear();

        return "redirect:/order/" + order.getId();
    }

    @GetMapping("/order/{id}")
    public String finish(@PathVariable("id") Long id, Model model) {
        Order order = orderDao.findById(id).orElseThrow();
        model.addAttribute("order", order);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Instant instantDelivery = order.getDateTime().plus(1, ChronoUnit.HOURS);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instantDelivery, ZoneId.systemDefault());
        String timeDelivery = localDateTime.format(formatter);
        model.addAttribute("timeDelivery", instantDelivery);

        return "pizzaToOrder/order";

    }
}

