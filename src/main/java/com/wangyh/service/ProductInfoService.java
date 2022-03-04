package com.wangyh.service;

import com.github.pagehelper.PageInfo;
import com.wangyh.pojo.ProductInfo;
import com.wangyh.pojo.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {

    //显示全部商品（不分页）
    List<ProductInfo> getAll();

    //分页功能实现
    PageInfo splitPage(int pageNum, int pageSize);

    //保存新增商品
    int save(ProductInfo info);

    //按主键id查询商品
    ProductInfo getByID(int pid);

    //更新商品
    int update(ProductInfo info);

    //单个商品删除
    int delete(int pid);

    //批量删除商品
    int deleteBatch(String[] ids);

    //多条件商品查询，不用了，保留着
    List<ProductInfo> seleteCondition(ProductInfoVo vo);

    //多条件商品查询分页
    PageInfo splitPageVo(ProductInfoVo vo,int pageSize);


}
