package bocai.com.yanghuaji.model;

/**
 * 获取植物状态的请求model
 * 作者 yuanfei on 2017/12/1.
 * 邮箱 yuanfei221@126.com
 */

public class PlantStatusModel {
    /**
     * EC : 1
     * CMD : getStatus
     * Temp : 1
     * UUID : 1504608600
     * WaterStat : 1
     * Pid : 1
     */

    private String EC;
    private String CMD;
    private String Temp;
    private String UUID;
    private String WaterStat;
    private String Pid;

    public PlantStatusModel(String EC, String CMD, String temp, String UUID, String waterStat, String pid) {
        this.EC = EC;
        this.CMD = CMD;
        Temp = temp;
        this.UUID = UUID;
        WaterStat = waterStat;
        Pid = pid;
    }

    public String getEC() {
        return EC;
    }

    public void setEC(String EC) {
        this.EC = EC;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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
