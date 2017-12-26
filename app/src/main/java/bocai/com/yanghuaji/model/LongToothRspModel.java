package bocai.com.yanghuaji.model;

/**
 * 作者 yuanfei on 2017/12/1.
 * 邮箱 yuanfei221@126.com
 */

public class LongToothRspModel {
    public static final int SUCCEED = 0;
    public static final int BIND_FAILED = 1;
    public static final int EQUIPMENT_HAVE_BINDED = 2;
    private int CODE;
    private int updateStat;
    private String softVer;

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }

    public int getUpdateStat() {
        return updateStat;
    }

    public void setUpdateStat(int updateStat) {
        this.updateStat = updateStat;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public int getCODE() {
        return CODE;
    }
}
