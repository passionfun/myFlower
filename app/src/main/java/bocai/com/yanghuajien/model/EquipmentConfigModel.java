package bocai.com.yanghuajien.model;

/**
 * 作者 yuanfei on 2017/12/19.
 * 邮箱 yuanfei221@126.com
 */
public class EquipmentConfigModel {

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
