package com.real.util;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//对密码进行加盐加密的工具类
@Component
public class Md5Util {
    public String md5(String password){
        //1. spring 提供的盐值加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //2.将传来的密码进行盐值加密，并获得加密串
        //加密的字符随机，但是可以和目标字符串匹配出来
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * @param password 前端传递的密码
     * @param mypwd 数据库中的密码
     * @return 返回比较结果
     */
    public boolean compare(String password,String mypwd){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password,mypwd);
    }
}
