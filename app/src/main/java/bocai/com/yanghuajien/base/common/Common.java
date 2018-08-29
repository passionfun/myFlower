package bocai.com.yanghuajien.base.common;



public class Common {
    /**
     * 一些不可变的永恒的参数
     */
    public interface Constance {
        // 手机号的正则,11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";
        //英文版服务器地址
        String API_URL = "http://47.90.188.18/web/api/index.php/";
        //H5英文版地址
        String H5_BASE = "http://47.90.188.18/web/h5/yhj/";

        //H5英文版测试地址
//        String H5_BASE = "http://demo.188388.cn:8082/yhjen/web/h5/yhj/";
        //英文版测试服务器地址
//        String API_URL = "http://demo.188388.cn:8082/yhjen/web/api/index.php/";
        //中文版地址
        //String H5_CN_BASE = http://47.98.46.78/web/h5/yhj/
        //接口地址 ：http://demo.188388.cn:8082/yhj/web/yhj/_book/login.html
    }
}
