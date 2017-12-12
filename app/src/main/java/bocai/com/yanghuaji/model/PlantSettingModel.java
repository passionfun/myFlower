package bocai.com.yanghuaji.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import bocai.com.yanghuaji.model.db.AppDatabase;

/**
 * Created by apple on 17-11-27.
 */
@Table(database = AppDatabase.class)
public class PlantSettingModel extends BaseModel{

    /**
     * Id : 3
     * PlantMode : 智能
     * PlantName : 含羞草
     * LifeCycle : 种子期
     * PMid : 种植模式id
     * Lid : 生长周期id
     * Pid : 植物id
     */
    @PrimaryKey
    private String Id;
    @Column
    private String PlantMode;
    @Column
    private String PlantName;
    @Column
    private String LifeCycle;
    @Column
    private String Pid;
    @Column
    private String Lid;
    @Column
    private String PMid;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPlantMode() {
        return PlantMode;
    }

    public void setPlantMode(String PlantMode) {
        this.PlantMode = PlantMode;
    }

    public String getPlantName() {
        return PlantName;
    }

    public void setPlantName(String PlantName) {
        this.PlantName = PlantName;
    }

    public String getLifeCycle() {
        return LifeCycle;
    }

    public void setLifeCycle(String LifeCycle) {
        this.LifeCycle = LifeCycle;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getLid() {
        return Lid;
    }

    public void setLid(String lid) {
        Lid = lid;
    }

    public String getPMid() {
        return PMid;
    }

    public void setPMid(String PMid) {
        this.PMid = PMid;
    }
}
