package com.vrp.distributesystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 自动提供get与set函数
@NoArgsConstructor // 自动提供无参构造
@AllArgsConstructor // 自动创建有参构造
public class User {
    private int id;
    private String username;
    @JsonIgnore // 不展示密码
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
}
