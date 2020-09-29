package com.ngu.product.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTOOutPut {

    private String productId;

    private Integer productQuantity;
}
