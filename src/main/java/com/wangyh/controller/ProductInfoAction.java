package com.wangyh.controller;

import com.github.pagehelper.PageInfo;
import com.wangyh.pojo.ProductInfo;
import com.wangyh.pojo.vo.ProductInfoVo;
import com.wangyh.service.ProductInfoService;
import com.wangyh.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {

    //每页显示的记录数
    public static final int PAGE_SIZE = 5;
    //异步上传的图片的名称
    String saveFileName = "";

    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品（不分页）
    @RequestMapping("getAll")
    public String getAll(Model model) {
        List<ProductInfo> list = productInfoService.getAll();
        model.addAttribute("list", list);
        return "product";
    }

    //默认显示第1页
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", info);
        return "product";
    }

    //ajax分页翻页处理
    @RequestMapping("/ajaxsplit")
    @ResponseBody
    public void ajaxsplit(ProductInfoVo vo, HttpSession session) {
        //取得当前页面page参数的页面的数据
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //异步Ajax文件上传处理
    @RequestMapping("/ajaxImg")
    @ResponseBody
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名UUID+上传图片的后缀.jpg .png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");
        try {
            //转存  C:\idea_workspace\mimissm\image_big\3c1c4296260f45b5b6acbe0e3f0649e4.jpg
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回客户端JSON对象，封装图片的路径，为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);

        return object.toString();
    }

    //保存新增商品
    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的5个数据，有异步Ajax上传的图片名称数据，有上架时间数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "增加成功！");
        } else {
            request.setAttribute("msg", "增加失败！");
        }
        //清空saveFileName变量中的内容，为了下次增加或修改的异步Ajax的上传处理
        saveFileName = "";
        //增加成功后应该重新访问数据库，所以跳转到分页显示的action上
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session) {
        ProductInfo info = productInfoService.getByID(pid);
        model.addAttribute("prod", info);
        //将多条件及页码放入session中，更新结束后分页时读取条件和页面进行处理。
        session.setAttribute("prodVo", vo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        //因为Ajax的异步图片上传，如果有上传过，则saveFileName里面有上传的图片名称,saveFileName不为空；
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        //完成更新处理
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "更新成功！");
        } else {
            request.setAttribute("msg", "更新失败！");
        }
        //清空saveFileName变量中的内容，为了下次增加或修改的异步Ajax的上传处理
        saveFileName = "";

        return "forward:/prod/split.action";
    }

    //单个商品删除
    @RequestMapping("/delete")
    public String delete(int pid, ProductInfoVo vo, HttpServletRequest request) {
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            request.setAttribute("msg", "删除成功！");
            request.getSession().setAttribute("deleteProdVo", vo);
        } else {
            request.setAttribute("msg", "删除失败！");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }

    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object deleteAjaxSplit(HttpServletRequest request) {
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");
    }

    //批量商品删除
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpServletRequest request) {
        //pids="1,3,4"  ids=[1,3,4]
        String[] ps = pids.split(",");
        int num = 0;
        try {
            num = productInfoService.deleteBatch(ps);
            if (num > 0) {
                request.setAttribute("msg", "批量删除成功！");
            } else {
                request.setAttribute("msg", "批量删除失败！");
            }
        } catch (Exception e) {
            request.setAttribute("msg", "商品不可删除！");
        }

        return "forward:/prod/deleteAjaxSplit.action";
    }

    //多条件查询功能实现
    @RequestMapping("/condition")
    @ResponseBody
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.seleteCondition(vo);
        session.setAttribute("list", list);
    }

}
