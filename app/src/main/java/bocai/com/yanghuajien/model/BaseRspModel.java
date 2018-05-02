package bocai.com.yanghuajien.model;

/**
 * 作者 yuanfei on 2017/11/16.
 * 邮箱 yuanfei221@126.com
 */

public class BaseRspModel<T> {


    /**
     * ReturnTime : 1510731467
     * ReturnCode : 200
     * Msg : 注册成功
     * Secure : 0
     * Data : t
     */

    private int ReturnTime;
    private String ReturnCode;
    private String Msg;
    private int Secure;
    private T Data;

    public int getReturnTime() {
        return ReturnTime;
    }

    public void setReturnTime(int ReturnTime) {
        this.ReturnTime = ReturnTime;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getSecure() {
        return Secure;
    }

    public void setSecure(int Secure) {
        this.Secure = Secure;
    }

    public T getData() {
        return Data;
    }

    public void setData(T Data) {
        this.Data = Data;
    }
}

