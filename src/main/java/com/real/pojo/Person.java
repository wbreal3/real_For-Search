package com.real.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//映射数据库的实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private int id;
    private String name;
    private String password;
    private String myImg;  //默认使用自带的
    private int musicId;   //默认id  1433430841（映射悬浮窗的音乐id）
}
