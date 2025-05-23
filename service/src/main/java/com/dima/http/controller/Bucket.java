package com.dima.http.controller;

import com.dima.dto.PizzaToOrderInBucket;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Bucket {

   private List<PizzaToOrderInBucket> pizzas = new ArrayList<>();

   public  BigDecimal getPriceOrder() {
      BigDecimal priceOrder = BigDecimal.valueOf(0.0);
      List<BigDecimal> priceList = pizzas.stream().map(PizzaToOrderInBucket::getPriceInBucket).toList();
      for (BigDecimal price : priceList) {
         priceOrder = priceOrder.add(price);
      }
      return priceOrder;
   }
}
