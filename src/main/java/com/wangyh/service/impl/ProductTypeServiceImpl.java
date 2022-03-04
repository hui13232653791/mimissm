package com.wangyh.service.impl;

import com.wangyh.dao.ProductTypeMapper;
import com.wangyh.pojo.ProductType;
import com.wangyh.pojo.ProductTypeExample;
import com.wangyh.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
