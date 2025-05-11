package com.dima.http.controller;

import com.dima.dto.PizzaToOrderInBucket;
import com.dima.dto.UserReadDto;
import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import com.dima.dto.PizzaIngredientReadDto;
import com.dima.dto.PizzaReadDto;
import com.dima.dto.PizzaToOrderCreateEditDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.math.BigDecimal;
import java.time.Instant;
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
                             @SessionAttribute Bucket bucket) {
        model.addAttribute("bucketList", bucket.pizzaToOrderInBucketList);

        return "pizzaToOrder/bucket";
    }

    @PostMapping("/bucket/add")
    public String bucketAdd(PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto,
                            PizzaToOrderInBucket pizzaToOrderInBucket,
                            Bucket bucket) {
        pizzaToOrderInBucket = pizzaToOrderService.prepareToCheckout(pizzaToOrderCreateEditDto, pizzaToOrderInBucket);
        bucket.pizzaToOrderInBucketList.add(pizzaToOrderInBucket);

        return "redirect:/bucket";
    }

    @GetMapping("/checkout")
    public String checkoutView(Model model,
                               Bucket bucket) {
        model.addAttribute("bucketList", bucket.pizzaToOrderInBucketList);
        model.addAttribute("priceOrder", getPriceOrder(bucket.pizzaToOrderInBucketList));

        return "pizzaToOrder/checkout";
    }

    @PostMapping("/checkout/add")
    public String checkoutAdd(Model model,
                              Bucket bucket) {
        model.addAttribute("bucketList", bucket.pizzaToOrderInBucketList);
        model.addAttribute("priceOrder", getPriceOrder(bucket.pizzaToOrderInBucketList));

        return "redirect:/checkout";
    }

    @PostMapping("/finish/add")
    public String finishAdd(Model model,
                            Bucket bucket) {
        model.addAttribute("bucketList", bucket.pizzaToOrderInBucketList);
        model.addAttribute("priceOrder", getPriceOrder(bucket.pizzaToOrderInBucketList));
        pizzaToOrderService.create(bucket.pizzaToOrderInBucketList);
        bucket.pizzaToOrderInBucketList.clear();

        return "redirect:/finish";
    }

    @GetMapping("/finish")
    public String finish(Model model,
                         Bucket bucket) {
        model.addAttribute("timeDelivery", Instant.now().plus(1L, ChronoUnit.HOURS).truncatedTo(ChronoUnit.MINUTES));
        model.addAttribute("priceOrder", getPriceOrder(bucket.pizzaToOrderInBucketList));

        return "pizzaToOrder/finish";
    }

    public static BigDecimal getPriceOrder(List<PizzaToOrderInBucket> pizzaToOrderInBucketList) {
        BigDecimal priceOrder = BigDecimal.valueOf(0.0);
        List<BigDecimal> priceList = pizzaToOrderInBucketList.stream().map(PizzaToOrderInBucket::getPriceInBucket).toList();
        for (BigDecimal price : priceList) {
            priceOrder = priceOrder.add(price);
        }
        return priceOrder;
    }
}

