package com.real.controller;

import com.real.pojo.AddUrl;
import com.real.pojo.AddUser;
import com.real.pojo.Person;
import com.real.pojo.Url;
import com.real.service.ServiceDao;
import com.real.util.CommonUtils;
import com.real.util.Md5Util;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 功能较为简单，直接写进controller中
 */

@Controller
public class Page {
    private int threadLocal;
    static Jedis jedis = new Jedis("127.0.0.1", 6379);

    //操作业务层，调用其中的方法
    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private Md5Util md5Util;
    //跳转页面的切换
    String re;
    //musicId的拼接
    String musicUrl;

    //欢迎页
    @RequestMapping("/")
    public String WellCome(HttpSession session){
        return "index";
    }

    //利用ajax异步判断用户名或者密码是否存在 正确
    @RequestMapping("/ajax")
    @ResponseBody
    public String ajax(String username){
        Person person = serviceDao.selectName(username);
        String msg;
        if(person ==null || !username.equals(person.getName())){
            msg="ok";
        }else {
            msg="用户已存在";
        }
        return msg;
    }
    //用来登录验证
    @RequestMapping("/ajaxToLogin")
    @ResponseBody
    public String ajaxToLogin(String username){
        Person person = serviceDao.selectName(username);
        String msg;
        if(person ==null || !username.equals(person.getName())){
            msg="没有此用户";
        }else {
            msg="ok";
        }
        return msg;
    }

    //跳转登录表单 login
    @RequestMapping("/loginFrom")
    public String from(){
        return "login";
    }

    //拿到用户的名字，musicId，收藏的网址信息
    @RequestMapping("/toLogin")
    public String toPage(String username, String password, Model model){
        //调用业务层代码，实现查找数据库  比对用户信息
        Person person = serviceDao.selectName(username);
        //使用spring自带的加盐加密
        //person 存在 并且 加密的密码相同就成功跳转
        if(person !=null && md5Util.compare(password,person.getPassword())){
            //如果这个用户存在，增加url类查询对应的url信息
            threadLocal=person.getId();
            List<Url> url = serviceDao.selectUrl(person.getId());
            //从数据库中拿到音乐id
            musicUrl = "//music.163.com/outchain/player?type=2&id="+person.getMusicId()+"&auto=0&height=66";
            model.addAttribute("musicId",musicUrl);
            model.addAttribute("music",person.getMusicId());
            //用户名传送
            model.addAttribute("userName",person.getName());
            //拿到该用户对应的保存的url信息
            model.addAttribute("lists",url);
            //登录成功直接跳转到自定义首页
            return  "home";

        }else {
            model.addAttribute("msg","用户名或密码错误");
            //登录不成功重定向到表单页面，并且携带msg数据
            return "login";
        }
        //用户登录成功，跳转到自定义个人首页

    }

    //用户注册  和用户登录公用一个html
    //引入业务层，调用注册用户的方法
    @RequestMapping("/toRegister")
    public String toRegister(HttpServletRequest request,Model model){
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
        // 设置获取key
        String key = getCaptchaKey(request);
        // 存入redis并设置过期时间为10分钟
        jedis.set(key,verCode);
        jedis.expire(key,60*10);
        model.addAttribute("send",specCaptcha.text());
        return "register";
    }
    @RequestMapping("/Register")
    public String Register(HttpServletRequest request,String username,String password,String verificationCode,Model model){
        // 获取key
        String key = getCaptchaKey(request);
        //从redis中获取key的值
        String value = jedis.get(key);
        //后面用这个值与前端输入的值做对比

        String passwordEncode = md5Util.md5(password);
        //将加密的密码存入数据库，
        if (serviceDao.selectName(username) != null){
            model.addAttribute("msg","用户已经存在");
            return "index";
        }else if(!value.equals(verificationCode)){
            model.addAttribute("msg","验证码错误");
            return "index";
        }
        else {
            AddUser user = new AddUser(username,passwordEncode);
            int i = serviceDao.addUser(user);
            if(i != 0){
                model.addAttribute("msg","用户注册成功，请登录！");
            }
            return "index";
        }
    }
    private String getCaptchaKey(HttpServletRequest request){
        // 获取用户ip地址
        String ip = CommonUtils.getIpAddr(request);
        // 获取浏览器请求头
        String userAgent = request.getHeader("User-Agent");
        String key = "user-service:captcha:"+CommonUtils.MD5(ip+userAgent);
        return key;
    }
    //跳转到用户自定义的首页
    @RequestMapping("/toUser")
    public String toHome(){
        return "home";
    }

    //跳转用户的个人信息页
    @RequestMapping("/toUserPage")
    //通过当前用户id携带用户的属性
    public String toUserPage(Model model){
        Person person = serviceDao.selectUserById(threadLocal);
        List<Url> urls = serviceDao.selectUrl(threadLocal);
        model.addAttribute("username",person.getName());
        model.addAttribute("music",person.getMusicId());
        model.addAttribute("password",person.getPassword());
        model.addAttribute("lists",urls);
        return "userPage";
    }

    //修改用户个人信息  成功后跳转本来页面显示修改成功
    @RequestMapping("/toUpdateUser")
    public String toUpdateUser(String name,int musicId,String password,Model model){
        //引入业务层，调用其修改用户的方法
        String passwordEncode = md5Util.md5(password);
        int i = serviceDao.updateUser(name, musicId, passwordEncode,threadLocal);
        model.addAttribute("msg","用户信息已修改，请重新登录");
        return "index";
    }

    //退出登录（删除cookie）返回主页面
    //直接把用户删除
    @RequestMapping("/logout")
    public String logout(Model model){
        serviceDao.deleteUser(threadLocal);
        model.addAttribute("msg","用户已注销");
        return "index";
    }

    //跳转到增加网页的页面（同时查询所有的网址）
    //同时实现网址的分页展示
    @RequestMapping("/addUrl")
    public String addUrl(Integer pageNum,Model model){
        //用pageNum表示当前在第几条数据的位置
        if(pageNum<0){
            pageNum=0;
        }
        //从哪个索引位置进行查找
        List<AddUrl> urls = serviceDao.selectAllUrl(pageNum);
        model.addAttribute("lists",urls);
        //把当前index返回给前端，方便定位上一页下一页时使用
        model.addAttribute("pageNumPre",pageNum-6);
        model.addAttribute("pageNumNex",pageNum+6);
        return "allUrl";
    }

    //用户添加这个页面到用户数据中
    @RequestMapping("/addTheUrl")
    @ResponseBody
    public void addTheUrl(String urlName,String url,String imgName,HttpServletResponse response) throws IOException {
        int userId=threadLocal;
        AddUrl addUrl = new AddUrl(urlName,url,imgName, userId);
        serviceDao.insertById(addUrl);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.flush();
        out.println("<script>");
        out.println("alert('添加完成，请刷新页面后浏览！');");
        out.println("window.opener=null;window.top.open('','_self','');window.close(this);");
        out.println("</script>");
    }
    //删除指定的url
    @RequestMapping("/deleteTheUrl")
    @ResponseBody
    public void deleteTheUrl(int urlId, HttpServletResponse response) throws IOException {
        //确认用户id和url的id，然后删除
        serviceDao.deleteTheUrl(threadLocal,urlId);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.flush();
        out.println("<script>");
        out.println("alert('已删除，请刷新页面！');");
        out.println("window.opener=null;window.top.open('','_self','');window.close(this);");
        out.println("</script>");
    }
}