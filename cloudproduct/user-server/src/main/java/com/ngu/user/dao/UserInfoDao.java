package com.ngu.user.dao;

import com.ngu.user.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoDao extends JpaRepository<UserInfo, String> {

    UserInfo findUserInfoByOpenid(String openid);
}
