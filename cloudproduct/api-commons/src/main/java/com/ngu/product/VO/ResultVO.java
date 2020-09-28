package com.ngu.product.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;
}
