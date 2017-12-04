package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/12/4.
 * 邮箱 yuanfei221@126.com
 */

public class LightOnModel {

    /**
     * CMD : lightOn
     * Begin : 73800
     * UUID : 1504608600
     */

    private String CMD;//lightOn 设置补光操作
    private String Begin; //补光开始时间
    private String UUID;//时间戳

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
}
