package com.ngu.user.service.impl;

import com.ngu.user.dao.UserInfoDao;
import com.ngu.user.domain.UserInfo;
import com.ngu.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo findUserInfoByOpenid(String openid) {
        return userInfoDao.findUserInfoByOpenid(openid);
    }
}
