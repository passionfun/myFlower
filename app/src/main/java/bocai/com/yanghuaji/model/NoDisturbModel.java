package bocai.com.yanghuaji.model;

/**
 * 设置设备免打扰时间的model
 * 作者 yuanfei on 2017/12/4.
 * 邮箱 yuanfei221@126.com
 */

public class NoDisturbModel {


    /**
     * CMD : noDisturb//免打扰
     * Begin : 73800 //免打扰开始时间
     * UUID : 1504608600//时间戳
     * End : 28800//免打扰结束时间（一天中的时间点，以秒为单位 比如下午8点30分 传的值为：20*60*60+30*60 =73800。免打扰周期不得超过24 小时-补光时长）
     */

    private String CMD;
    private String Begin;
    private String UUID;
    private String End;

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getBegin() {
        return Begin;
    }

    public void setBegin(String begin) {
        Begin = begin;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }
}
