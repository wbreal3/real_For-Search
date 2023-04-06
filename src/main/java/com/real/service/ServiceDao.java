package com.real.service;

import com.real.pojo.AddUrl;
import com.real.pojo.AddUser;
import com.real.pojo.Person;
import com.real.pojo.Url;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

//service总接口，定义方法集合
@Service
public interface ServiceDao {
    //增
    int addUser(AddUser person);
    //删
    int deleteUser(int id);
    //改,添加用户
    int updateUser(String name,int musicId,String password,int id);
    //查,多个参数是用@Param修饰
    Person selectUser(@Param("username")String username,@Param("password")String password);
    //通过id查找用户
    Person selectUserById(int id);
    Person selectName(String username);
    //用户id添加url
    int insertById(AddUrl url);
    //增加url
    int addUrl(AddUrl addUrl);
    //查询用户对应的url（通过这个用户的id对应url中userId）
    List<Url> selectUrl(int id);
    //查询所有的网址（用于用户的添加）
    List<AddUrl> selectAllUrl(int index);
    //删除用户的url
    int deleteTheUrl(int userId,int urlId);
}
