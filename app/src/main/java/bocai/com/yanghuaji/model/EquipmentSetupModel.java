package bocai.com.yanghuaji.model;

/**
 * Created by apple on 17-11-28.
 */

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import bocai.com.yanghuaji.model.db.AppDatabase;

@Table(database = AppDatabase.class)
public class EquipmentSetupModel extends BaseModel{

    /**
     * Id : 3  设备id
     * EquipName : WG222+2222
     * WaterMode : 1
     * BanStart : 12:00
     * BanStop : 16:04
     * GroupName : 阳台
     */
    @PrimaryKey
    private String Id;
    @Column
    private String EquipName;
    @Column
    private String WaterMode;
    @Column
    private String BanStart;
    @Column
    private String BanStop;
    @Column
    private String GroupName;
    @Column
    private String LightStart;

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



    public String getLightStart() {
        return LightStart;
    }

    public void setLightStart(String lightStart) {
        LightStart = lightStart;
    }
}
