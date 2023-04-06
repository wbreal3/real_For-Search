package com.real.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//用户对应收藏url
public class Url {
    private int urlId;
    private int userId;
    private String urlName;
    private String url;
    private String imgName;
}
