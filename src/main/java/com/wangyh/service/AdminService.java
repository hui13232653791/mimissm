package com.wangyh.service;

import com.wangyh.pojo.Admin;

public interface AdminService {

    //完成登录判断
    Admin login(String name, String pwd);

}
