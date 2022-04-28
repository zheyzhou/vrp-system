package com.vrp.distributesystem.service;

import com.vrp.distributesystem.entity.User;
import com.vrp.distributesystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int save(User user){
        if (user.getId() == 0){ // user没有id，表示新增
            return userMapper.insert(user);
        } else { // 否则为更新
            return userMapper.update(user);
        }
    }

}
