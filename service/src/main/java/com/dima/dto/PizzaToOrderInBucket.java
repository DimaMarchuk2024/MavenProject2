package com.dima.dto;

import com.dima.enumPack.Size;
import com.dima.enumPack.TypeDough;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PizzaToOrderInBucket {

   private String pizzaName;
   private Integer pizzaId;
   private Size size;
   private TypeDough type;
   private BigDecimal priceInBucket;
   private Integer count;
   private Long userId;
   private List<Integer> ingredients;
   private List<String> ingredientName = new ArrayList<>();
}
