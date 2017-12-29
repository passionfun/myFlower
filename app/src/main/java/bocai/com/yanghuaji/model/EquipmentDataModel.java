package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/12/7.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentDataModel {


    /**
     * Degree : 30//温度
     * Water : 20 水位
     * Light : 1 光照状态  0关  1开
     * Ec : 900 营养值
     * Dstatus : 1 温度状态   0过低  1正常  2过高
     * Wstatus : 1 水位状态   0过低  1正常  2过高
     * Estatus : 1 营养值状态   0过低  1正常  2过高
     */

    private String Degree;
    private String Water;
    private String Light;
    private String Ec;
    private String Dstatus;
    private String Wstatus;
    private String Estatus;
    private String LTID;


    public String getDegree() {
        return Degree;
    }

    public void setDegree(String Degree) {
        this.Degree = Degree;
    }

    public String getWater() {
        return Water;
    }

    public void setWater(String Water) {
        this.Water = Water;
    }

    public String getLight() {
        return Light;
    }

    public void setLight(String Light) {
        this.Light = Light;
    }

    public String getEc() {
        return Ec;
    }

    public void setEc(String Ec) {
        this.Ec = Ec;
    }

    public String getDstatus() {
        return Dstatus;
    }

    public void setDstatus(String dstatus) {
        Dstatus = dstatus;
    }

    public String getWstatus() {
        return Wstatus;
    }

    public void setWstatus(String wstatus) {
        Wstatus = wstatus;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }

    public String getLTID() {
        return LTID;
    }

    public void setLTID(String LTID) {
        this.LTID = LTID;
    }

    @Override
    public String toString() {
        return "EquipmentDataModel{" +
                "Degree='" + Degree + '\'' +
                ", Water='" + Water + '\'' +
                ", Light='" + Light + '\'' +
                ", Ec='" + Ec + '\'' +
                ", Dstatus='" + Dstatus + '\'' +
                ", Wstatus='" + Wstatus + '\'' +
                ", Estatus='" + Estatus + '\'' +
                ", LTID='" + LTID + '\'' +
                '}';
    }
}
