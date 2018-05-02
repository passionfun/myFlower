package bocai.com.yanghuajien.model;

/**
 * 获取植物状态成功后返回model
 * 作者 yuanfei on 2017/12/4.
 * 邮箱 yuanfei221@126.com
 */

public class PlantStatusRspModel {


    /**
     * CODE : 0
     * EC : 600
     * Temp : 28
     * WaterStat : 1
     * Pid : 101
     */

    private int CODE;
    private String EC;
    private String Temp;
    private String WaterStat;
    private String Pid;

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public String getEC() {
        return EC;
    }

    public void setEC(String EC) {
        this.EC = EC;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getWaterStat() {
        return WaterStat;
    }

    public void setWaterStat(String waterStat) {
        WaterStat = waterStat;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }
}
