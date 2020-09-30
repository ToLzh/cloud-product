package com.ngu.user.controller;

import com.ngu.product.VO.ResultVO;
import com.ngu.product.constant.CookieConstant;
import com.ngu.product.utils.CookieUtil;
import com.ngu.product.utils.ResultVOUtil;
import com.ngu.product.constant.RedisConstant;
import com.ngu.user.domain.UserInfo;
import com.ngu.user.enums.RoleEnum;
import com.ngu.user.enums.UserExceptionEnum;
import com.ngu.user.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("login")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("buyer")
    public ResultVO buyer(@RequestParam("openid") String openid,
                          HttpServletResponse response){
        UserInfo user = userService.findUserInfoByOpenid(openid);
        if (user == null) {
            return ResultVOUtil.error(UserExceptionEnum.LOGIN_FAIL.getMsg());
        }
        if (user.getRole() != RoleEnum.BUYER.getCode()) {
            return ResultVOUtil.error(UserExceptionEnum.ROLE_ERROR.getMsg());
        }
        CookieUtil.set(response, CookieConstant.OPENID,openid,CookieConstant.EXPIRE);

        return ResultVOUtil.success(null);
    }

    @GetMapping("seller")
    public ResultVO seller(@RequestParam("openid") String openid,
                          HttpServletRequest request,
                          HttpServletResponse response){
        // 判断是否一登录
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null && !StringUtils.isEmpty(cookie.getValue())) {
            String value = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE, cookie.getValue()));
            if (!StringUtils.isEmpty(value)){
                return ResultVOUtil.success(null);
            }
        }

        UserInfo user = userService.findUserInfoByOpenid(openid);
        if (user == null) {
            return ResultVOUtil.error(UserExceptionEnum.LOGIN_FAIL.getMsg());
        }
        if (user.getRole() != RoleEnum.SELLER.getCode()) {
            return ResultVOUtil.error(UserExceptionEnum.ROLE_ERROR.getMsg());
        }

        // 写入redis   key = UUID  VALUE = OPENID
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.EXPIRE;
        stringRedisTemplate.opsForValue().set(
            String.format(RedisConstant.TOKEN_TEMPLATE,token),
            openid,
            expire,
            TimeUnit.SECONDS
        );

        // 写入cookie
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.EXPIRE);

        return ResultVOUtil.success(null);
    }
}
