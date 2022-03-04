import com.wangyh.dao.ProductInfoMapper;
import com.wangyh.pojo.ProductInfo;
import com.wangyh.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestSelete {

    @Autowired
    ProductInfoMapper productInfoMapper;

    //测试多条件查询
    @Test
    public void testSeleteCondition(){
        ProductInfoVo vo = new ProductInfoVo();
        vo.setPname("4");
        vo.setTypeid(3);
        vo.setLprice(3000);
        vo.setHprice(4000);
        List<ProductInfo> list = productInfoMapper.seleteCondition(vo);
        list.forEach(productInfo -> System.out.println(productInfo));
    }

}
