package bocai.com.yanghuaji.model;

/**
 * Created by apple on 17-11-28.
 */

public class EquipmentSetupModel {

    /**
     * Id : 3
     * EquipName : WG222+2222
     * WaterMode : 1
     * BanStart : 12:00
     * BanStop : 16:04
     * GroupName : 阳台
     */

    private String Id;
    private String EquipName;
    private String WaterMode;
    private String BanStart;
    private String BanStop;
    private String GroupName;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getEquipName() {
        return EquipName;
    }

    public void setEquipName(String EquipName) {
        this.EquipName = EquipName;
    }

    public String getWaterMode() {
        return WaterMode;
    }

    public void setWaterMode(String WaterMode) {
        this.WaterMode = WaterMode;
    }

    public String getBanStart() {
        return BanStart;
    }

    public void setBanStart(String BanStart) {
        this.BanStart = BanStart;
    }

    public String getBanStop() {
        return BanStop;
    }

    public void setBanStop(String BanStop) {
        this.BanStop = BanStop;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }
}
