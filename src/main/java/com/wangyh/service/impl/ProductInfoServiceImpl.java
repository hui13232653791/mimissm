package com.wangyh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangyh.dao.ProductInfoMapper;
import com.wangyh.pojo.ProductInfo;
import com.wangyh.pojo.ProductInfoExample;
import com.wangyh.pojo.vo.ProductInfoVo;
import com.wangyh.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum,pageSize);
        //进行PageInfod的数据封装
        //进行条件的查询操作，必须创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，按主键降序排序
        example.setOrderByClause("p_id desc");
        //设置排序后，取集合
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getByID(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> seleteCondition(ProductInfoVo vo) {
        return productInfoMapper.seleteCondition(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(vo.getPage(),pageSize);
        //取集合
        List<ProductInfo> list = productInfoMapper.seleteCondition(vo);
        //集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
