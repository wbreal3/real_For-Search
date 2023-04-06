package com.real.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用于注册用户使用的实体类，不需要id和其他属性
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUser {
    private String username;
    private String password;
}
