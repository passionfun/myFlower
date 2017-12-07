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

    private int EC;
    private String CMD;
    private int Temp;
    private int UUID;
    private int WaterStat;
    private int Pid;


    public PlantStatusModel(int EC, String CMD, int temp, int UUID, int waterStat, int pid) {
        this.EC = EC;
        this.CMD = CMD;
        Temp = temp;
        this.UUID = UUID;
        WaterStat = waterStat;
        Pid = pid;
    }


    public int getEC() {
        return EC;
    }

    public void setEC(int EC) {
        this.EC = EC;
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String CMD) {
        this.CMD = CMD;
    }

    public int getTemp() {
        return Temp;
    }

    public void setTemp(int temp) {
        Temp = temp;
    }

    public int getUUID() {
        return UUID;
    }

    public void setUUID(int UUID) {
        this.UUID = UUID;
    }

    public int getWaterStat() {
        return WaterStat;
    }

    public void setWaterStat(int waterStat) {
        WaterStat = waterStat;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int pid) {
        Pid = pid;
    }
}
