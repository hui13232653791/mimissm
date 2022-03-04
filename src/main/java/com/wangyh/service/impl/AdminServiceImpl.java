package com.wangyh.service.impl;

import com.wangyh.dao.AdminMapper;
import com.wangyh.pojo.Admin;
import com.wangyh.pojo.AdminExample;
import com.wangyh.service.AdminService;
import com.wangyh.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        //根据用户名查用户对象回来，取出查回来的对象的密码和传来的密码对比，判断登录是否成功
        //创建条件封装的对象AdminExample
        AdminExample example = new AdminExample();
        //将用户名作为条件封装进AdminExample对象
        example.createCriteria().andANameEqualTo(name);

        List<Admin> list = adminMapper.selectByExample(example);
        if (list.size() > 0) {
            Admin admin = list.get(0);
            String md5Pwd = MD5Util.getMD5(pwd);
            if (md5Pwd.equals(admin.getaPass())) {
                return admin;
            }
        }
        return null;
    }
}
