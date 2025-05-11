package com.dima.service;

import com.dima.dao.impl.IngredientDao;
import com.dima.dao.impl.IngredientToOrderDao;
import com.dima.dao.impl.OrderDao;
import com.dima.dao.impl.OrderDetailDao;
import com.dima.dao.impl.PizzaToOrderDao;
import com.dima.dto.OrderReadDto;
import com.dima.dto.PizzaToOrderCreateEditDto;
import com.dima.dto.PizzaToOrderInBucket;
import com.dima.dto.PizzaToOrderReadDto;
import com.dima.entity.IngredientToOrder;
import com.dima.entity.Order;
import com.dima.entity.OrderDetail;
import com.dima.mapper.OrderReadMapper;
import com.dima.mapper.PizzaToOrderReadMapper;
import com.dima.mapper.PizzaToOtderCreateEditMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dima.http.controller.PizzaToOrderController.getPriceOrder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PizzaToOrderService {

    private final PizzaToOrderDao pizzaToOrderDao;
    private final PizzaToOrderReadMapper pizzaToOrderReadMapper;
    private final PizzaToOtderCreateEditMapper pizzaToOtderCreateEditMapper;
    private final IngredientToOrderDao ingredientToOrderDao;
    private final IngredientDao ingredientDao;
    private final PizzaService pizzaService;
    private final OrderDao orderDao;
    private final OrderReadMapper orderReadMapper;
    private final OrderDetailDao orderDetailDao;
    private final IngredientService ingredientService;


    public Page<PizzaToOrderReadDto> findAll(Pageable pageable) {
        return pizzaToOrderDao.findAll(pageable)
                .map(pizzaToOrderReadMapper::map);
    }

    public Optional<PizzaToOrderReadDto> findById(Long id) {
        return pizzaToOrderDao.findById(id)
                .map(pizzaToOrderReadMapper::map);
    }

    public BigDecimal getPriceInBucket(PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto) {
        BigDecimal priceInBucket = BigDecimal.valueOf(0.0);

        List<Integer> ingredientsIdsToBucket = pizzaToOrderCreateEditDto.getIngredients();
        for (int ingredientsIdToBucket : ingredientsIdsToBucket) {
            BigDecimal price = ingredientDao.findById(ingredientsIdToBucket).get().getPrice();
            priceInBucket = priceInBucket.add(price);
        }
        return priceInBucket;
    }

    public PizzaToOrderInBucket prepareToCheckout(PizzaToOrderCreateEditDto pizzaToOrderCreateEditDto, PizzaToOrderInBucket pizzaToOrderInBucket) {
        String pizzaName = pizzaService.findById(pizzaToOrderCreateEditDto.getPizzaId()).orElseThrow().getName();
        BigDecimal priceInBucket = getPriceInBucket(pizzaToOrderCreateEditDto);

        pizzaToOrderInBucket.setPizzaName(pizzaName);
        pizzaToOrderInBucket.setPizzaId(pizzaToOrderCreateEditDto.getPizzaId());
        pizzaToOrderInBucket.setSize(pizzaToOrderCreateEditDto.getSize());
        pizzaToOrderInBucket.setType(pizzaToOrderCreateEditDto.getType());
        pizzaToOrderInBucket.setPriceInBucket(priceInBucket);
        pizzaToOrderInBucket.setCount(pizzaToOrderCreateEditDto.getCount());
        pizzaToOrderInBucket.setUserId(pizzaToOrderCreateEditDto.getUserId());
        pizzaToOrderInBucket.setIngredients(pizzaToOrderCreateEditDto.getIngredients());

        for (Integer ingredientId : pizzaToOrderCreateEditDto.getIngredients()) {
            pizzaToOrderInBucket.getIngredientName().add(ingredientService.findById(ingredientId).orElseThrow().getName());
        }
        return pizzaToOrderInBucket;
    }

    @Transactional
    public void create(List<PizzaToOrderInBucket> pizzaToOrderInBucketList) {
        List<Long> listPizzaToOrderId = new ArrayList<>();

        for (PizzaToOrderInBucket pizzaToOrderInBucket : pizzaToOrderInBucketList) {
            PizzaToOrderReadDto pizzaToOrderReadDto = Optional.of(pizzaToOrderInBucket)
                    .map(pizzaToOtderCreateEditMapper::map)
                    .map(pizzaToOrderDao::save)
                    .map(pizzaToOrderReadMapper::map)
                    .orElseThrow();
            listPizzaToOrderId.add(pizzaToOrderReadDto.getId());

            Long pizzaToOrderId = pizzaToOrderReadDto.getId();
            List<Integer> ingredients = pizzaToOrderInBucket.getIngredients();
            for (Integer ingredient : ingredients) {
                IngredientToOrder ingredientToOrder = IngredientToOrder.builder()
                        .pizzaToOrder(pizzaToOrderDao.findById(pizzaToOrderId).orElseThrow())
                        .ingredient(ingredientDao.findById(ingredient).orElseThrow())
                        .build();
                ingredientToOrderDao.save(ingredientToOrder);
            }
        }
        Order order = Order.builder()
                .dateTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .finalPrice(getPriceOrder(pizzaToOrderInBucketList))
                .build();
        OrderReadDto orderReadDto = Optional.of(order)
                .map(orderDao::save)
                .map(orderReadMapper::map)
                .orElseThrow();

        for (Long pizzaToOrderId : listPizzaToOrderId) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(orderDao.findById(orderReadDto.getId()).orElseThrow())
                    .pizzaToOrder(pizzaToOrderDao.findById(pizzaToOrderId).orElseThrow())
                    .price(getPriceOrder(pizzaToOrderInBucketList))
                    .build();
            orderDetailDao.save(orderDetail);
        }
    }

        @Transactional
        public Optional<PizzaToOrderReadDto> update (Long id, PizzaToOrderInBucket pizzaToOrderInBucket){
            Optional<PizzaToOrderReadDto> pizzaToOrderReadDto = pizzaToOrderDao.findById(id)
                    .map(pizzaToOrder -> pizzaToOtderCreateEditMapper.map(pizzaToOrderInBucket, pizzaToOrder))
                    .map(pizzaToOrderDao::saveAndFlush)
                    .map(pizzaToOrderReadMapper::map);

            Long pizzaToOrderId = pizzaToOrderReadDto.get().getId();
            List<Integer> ingredients = pizzaToOrderInBucket.getIngredients();
            for (Integer ingredient : ingredients) {
                IngredientToOrder ingredientToOrder = IngredientToOrder.builder()
                        .pizzaToOrder(pizzaToOrderDao.findById(pizzaToOrderId).orElseThrow())
                        .ingredient(ingredientDao.findById(ingredient).orElseThrow())
                        .build();
                ingredientToOrderDao.saveAndFlush(ingredientToOrder);
            }
            return pizzaToOrderReadDto;
        }

    @Transactional
    public boolean delete(Long id) {
        return pizzaToOrderDao.findById(id)
                .map(pizzaToOrder -> {
                    pizzaToOrderDao.delete(pizzaToOrder);
                    pizzaToOrderDao.flush();
                    return true;
                })
                .orElse(false);
    }
}

