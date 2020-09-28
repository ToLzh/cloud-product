package com.ngu.product.utils;

import com.ngu.product.VO.ResultVO;

public class ResultVOUtil {

    public static ResultVO success(Object obj) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(200);
        resultVO.setMsg("成功");
        resultVO.setData(obj);
        return  resultVO;
    }
}
