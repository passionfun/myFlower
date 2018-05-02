package bocai.com.yanghuajien.model;

/**
 *
 * Created by shc on 2017/12/27.
 */

public class AddEquipmentsModel {


    /**
     * EquipName : WG_C460
     * Mac : B0:F8:93:10:C4:60
     * SerialNum : 8001F023412332
     * Version : 1.0.5
     * LTID : 1.1.2353.29.152
     * PSIGN : 1512125613
     * Series : WG101
     */

    private String EquipName;
    private String Mac;
    private String SerialNum;
    private String Version;
    private String LTID;
    private String PSIGN;
    private String Series;


    public AddEquipmentsModel(String equipName, String mac, String serialNum,
                              String version, String LTID, String PSIGN, String series) {
        EquipName = equipName;
        Mac = mac;
        SerialNum = serialNum;
        Version = version;
        this.LTID = LTID;
        this.PSIGN = PSIGN;
        Series = series;
    }

    public String getEquipName() {
        return EquipName;
    }

    public void setEquipName(String EquipName) {
        this.EquipName = EquipName;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String Mac) {
        this.Mac = Mac;
    }

    public String getSerialNum() {
        return SerialNum;
    }

    public void setSerialNum(String SerialNum) {
        this.SerialNum = SerialNum;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getLTID() {
        return LTID;
    }

    public void setLTID(String LTID) {
        this.LTID = LTID;
    }

    public String getPSIGN() {
        return PSIGN;
    }

    public void setPSIGN(String PSIGN) {
        this.PSIGN = PSIGN;
    }

    public String getSeries() {
        return Series;
    }

    public void setSeries(String Series) {
        this.Series = Series;
    }
}
