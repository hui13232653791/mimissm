package com.wangyh.controller;

import com.wangyh.pojo.Admin;
import com.wangyh.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminAction {

    @Autowired
    AdminService adminService;

    //实现登录判断，并进行相应的跳转
    @RequestMapping("/login")
    public String login(String name, String pwd, Model model) {
        Admin admin = adminService.login(name, pwd);
        if (admin!=null) {
            //登录成功
            model.addAttribute("admin", admin);
            return "main";
        } else {
            //登录失败
            model.addAttribute("errmsg", "用户名或密码不正确！");
            return "login";
        }
    }

}
