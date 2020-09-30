package com.ngu.user.service;

import com.ngu.user.domain.UserInfo;

public interface UserService {
    UserInfo findUserInfoByOpenid(String openid);
}
