package com.real.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用来增加url（映射）
@Data
@NoArgsConstructor
@AllArgsConstructor
//此方法用于修改
public class AddUrl {
    private String urlName;
    private String url;
    private String imgName;
    private int userId;
}
