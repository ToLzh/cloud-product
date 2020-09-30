package com.ngu.product.utils;

import com.ngu.product.VO.ResultVO;

public class ResultVOUtil {

    public static ResultVO success(Object obj) {
        ResultVO resultVO = new ResultVO();
        resultVO.setState("ok");
        resultVO.setCode(200);
        resultVO.setMsg("成功");
        resultVO.setData(obj);
        return  resultVO;
    }

    public static ResultVO error(String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setState("fail");
        resultVO.setCode(500);
        resultVO.setMsg(msg);
        resultVO.setData(null);
        return  resultVO;
    }

}
