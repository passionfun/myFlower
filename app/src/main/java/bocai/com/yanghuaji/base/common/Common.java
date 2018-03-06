package bocai.com.yanghuaji.base.common;



public class Common {
    /**
     * 一些不可变的永恒的参数
     */
    public interface Constance {
        // 手机号的正则,11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        // 测试服务器地址
//        String API_URL = "http://121.41.128.239:8082/yhj/web/api/index.php/";
        //正式服务器地址
        String API_URL = "http://47.98.46.78/web/api/index.php/";
        //测试地址
//        String H5_BASE = "http://121.41.128.239:8082/yhj/web/h5/yhj/";
        //正式地址 http://47.98.46.78/web/h5/yhj/product.html?id=123
        String H5_BASE = "http://47.98.46.78/web/h5/yhj/";


    }
}
