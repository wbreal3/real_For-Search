package com.real.mapper;

import com.real.pojo.AddUrl;
import com.real.pojo.AddUser;
import com.real.pojo.Person;
import com.real.pojo.Url;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//mybatis的mapper接口，业务层调用对应的方法
@org.apache.ibatis.annotations.Mapper
public interface Mapper {
    //增加用户
    int addUser(AddUser user);
    //删除用户
    int deleteUser(int id);
    //改,添加用户
    int updateUser(String name,int musicId,String password,int id);
    //查找单个用户 ,多个参数是用@Param修饰
    Person selectUser(@Param("username")String username, @Param("password")String password);
    //通过id查找用户
    Person selectUserById(int id);
    //用于ajax异步请求用户名是否存在时进行查找数据库
    Person selectName(String username);
    //通过用户id进行查找
    int insertById(AddUrl url);
    //增加url
    int addUrl(AddUrl addUrl);
    //查询用户对应的url（通过这个用户的id对应url中userId）
    List<Url> selectUrl(int id);
    //查询所有的url网址
    List<AddUrl> selectAllUrl(int index);
    //删除用户的url
    int deleteTheUrl(int userId,int urlId);
}
