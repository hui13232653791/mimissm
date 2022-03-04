import com.wangyh.utils.MD5Util;
import org.junit.Test;

public class TestMD5 {

    @Test
    public void testMD5(){
        String mi= MD5Util.getMD5("123456");
        System.out.println(mi);
    }

}
