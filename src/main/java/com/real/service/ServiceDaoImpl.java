package com.real.service;

import com.real.mapper.Mapper;
import com.real.pojo.AddUrl;
import com.real.pojo.AddUser;
import com.real.pojo.Person;
import com.real.pojo.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//业务层的实现类，将mapper接口引入实现对数据库的操作
@Service //使用注解将这个类交给springboot托管
public class ServiceDaoImpl implements ServiceDao{
    private Mapper mapper;
    @Autowired //利用注解将mapper注入这个类中
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int addUser(AddUser person) {
        return mapper.addUser(person);
    }

    @Override
    public int deleteUser(int id) {
        return mapper.deleteUser(id);
    }

    @Override
    public int updateUser(String name,int musicId,String password,int id) {
        return mapper.updateUser(name,musicId,password,id);
    }

    @Override
    public Person selectUser(String username, String password) {
        return mapper.selectUser(username,password);
    }

    @Override
    public Person selectUserById(int id) {
        return mapper.selectUserById(id);
    }

    @Override
    public Person selectName(String username) {
        return mapper.selectName(username);
    }

    @Override
    public int insertById(AddUrl url) {
        return mapper.insertById(url);
    }

    @Override
    public int addUrl(AddUrl addUrl) {
        return mapper.addUrl(addUrl);
    }

    @Override
    //用户的id
    public List<Url> selectUrl(int id) {
        return mapper.selectUrl(id);
    }

    @Override
    public List<AddUrl> selectAllUrl(int index) {
        return mapper.selectAllUrl(index);
    }

    @Override
    public int deleteTheUrl(int userId,int urlId) {
        return mapper.deleteTheUrl(userId,urlId);
    }

}
