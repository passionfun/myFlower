package bocai.com.yanghuajien.model;

/**
 * 作者 yuanfei on 2017/12/19.
 * 邮箱 yuanfei221@126.com
 */
public class EquipmentConfigModel {


    /**
     * DeveloperID : 2000110256
     * AppID : 1
     * AppKey : 30820126300D06092A864886F70D010101050003820113003082010E028201023030384139413344343732314442414231413230353836364343433436423939413737383337413934373936434541423141324143443644354333323137433737323537374134353532313642304232454433323642354342423235384630383433423042363346393335423830344642434530453531343043454234323437413233354336334145454643343933393837424231324143303546444235453445334639414141443531364338433133444646434343413845343837444241333035383435363738463745303635373433303233314233353337343231423545344544453136323842313231303645443938313732463036424339304545363136420206303130303031
     * ServiceName : longtooth
     * Port : 53180
     * RegisterHost : 114.215.170.184
     */

    private String DeveloperID;
    private String AppID;
    private String AppKey;
    private String ServiceName;
    private String Port;
    private String RegisterHost;

    public String getDeveloperID() {
        return DeveloperID;
    }

    public void setDeveloperID(String DeveloperID) {
        this.DeveloperID = DeveloperID;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String AppID) {
        this.AppID = AppID;
    }

    public String getAppKey() {
        return AppKey;
    }

    public void setAppKey(String AppKey) {
        this.AppKey = AppKey;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String ServiceName) {
        this.ServiceName = ServiceName;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String Port) {
        this.Port = Port;
    }

    public String getRegisterHost() {
        return RegisterHost;
    }

    public void setRegisterHost(String RegisterHost) {
        this.RegisterHost = RegisterHost;
    }

    @Override
    public String toString() {
        return "EquipmentConfigModel{" +
                "DeveloperID='" + DeveloperID + '\'' +
                ", AppID='" + AppID + '\'' +
                ", AppKey='" + AppKey + '\'' +
                ", ServiceName='" + ServiceName + '\'' +
                ", Port='" + Port + '\'' +
                ", RegisterHost='" + RegisterHost + '\'' +
                '}';
    }
}
