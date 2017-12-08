package bocai.com.yanghuaji.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentRspModel {

    /**
     * List : [{"Id":"1","Type":"0","Pstatus":"2","EquipName":"WG222+333","PlantName":"满天星","GroupName":"办公室","Days":132,"Degree":"26","Dstatus":"1","Wstatus":"0",
     * "Light":"1","Estatus":"1","Photo":"http://121.41.128.239:8082/yhj/web/upload/2017/11/20/151116546875djd66.jpg"},
     * {"Id":"2","Type":"0","Pstatus":"0","EquipName":"WG222+111","PlantName":null,"GroupName":"办公室","Days":null,"Degree":null,"Dstatus":null,
     * "Wstatus":null,"Light":null,"Estatus":null,"Photo":"http://121.41.128.239:8082/yhj/web/upload/2017/11/20/1511165402861mjqjl.png"},
     * {"Id":"3","Type":"0","Pstatus":"0","EquipName":"WG222+2222","PlantName":"含羞草","GroupName":null,"Days":null,"Degree":null,"Dstatus":null,"Wstatus":null,"Light":null,
     * "Estatus":null,"Photo":"http://121.41.128.239:8082/yhj/web/upload/2017/11/20/1511165402861mjqjl.png"}]
     * CountPage : 1
     * Count : 3
     *
     *
         * Id	设备id	String	false
     Pstatus	种植状态  0未种植  1种植中  2种植结束	String	false
     EquipName	设备名称	String	false
     PlantName	植物名称	String	true
     Pid        植物id
     GroupName	分组名称	String	true
     Days	记录天数	String	true
     Degree	温度	String	true
     Dstatus	温度状态  0偏低  1正常  2偏高	String	true
     Wstatus	水位状态  0偏低  1正常  2偏高	String	true
     Light	台灯   0关  1开	String	true
     Estatus	Ec状态  0偏低  1正常  2偏高	String	true
     Photo	图片url	String	true
     */

    private int CountPage;
    private int Count;
    private List<ListBean> List;

    public int getCountPage() {
        return CountPage;
    }

    public void setCountPage(int CountPage) {
        this.CountPage = CountPage;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean implements Serializable{
        /**
         * Id : 1
         * Type : 0
         * Pstatus : 2
         * EquipName : WG222+333
         * PlantName : 满天星
         * Pid:"2"
         * GroupName : 办公室
         * Days : 132
         * Degree : 26
         * Dstatus : 1
         * Wstatus : 0
         * Light : 1
         * Estatus : 1
         * Photo : http://121.41.128.239:8082/yhj/web/upload/2017/11/20/151116546875djd66.jpg
         * LTID
         * PSIGN
         *
         */

        private String Id;
        private String Type;
        private String Pstatus;
        private String EquipName;
        private String PlantName;
        private String Pid;
        private String GroupName;
        private int Days;
        private String Degree;
        private String Dstatus;
        private String Wstatus;
        private String Light;
        private String Estatus;
        private String Photo;
        private String Mac;
        private String LTID;
        private String PSIGN;

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

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getPstatus() {
            return Pstatus;
        }

        public void setPstatus(String Pstatus) {
            this.Pstatus = Pstatus;
        }

        public String getEquipName() {
            return EquipName;
        }

        public void setEquipName(String EquipName) {
            this.EquipName = EquipName;
        }

        public String getPid() {
            return Pid;
        }

        public void setPid(String Pid) {
            this.Pid = Pid;
        }

        public String getPlantName() {
            return PlantName;
        }

        public void setPlantName(String PlantName) {
            this.PlantName = PlantName;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public int getDays() {
            return Days;
        }

        public void setDays(int Days) {
            this.Days = Days;
        }

        public String getDegree() {
            return Degree;
        }

        public void setDegree(String Degree) {
            this.Degree = Degree;
        }

        public String getDstatus() {
            return Dstatus;
        }

        public void setDstatus(String Dstatus) {
            this.Dstatus = Dstatus;
        }

        public String getWstatus() {
            return Wstatus;
        }

        public void setWstatus(String Wstatus) {
            this.Wstatus = Wstatus;
        }

        public String getLight() {
            return Light;
        }

        public void setLight(String Light) {
            this.Light = Light;
        }

        public String getMac() {
            return Mac;
        }

        public void setMac(String mac) {
            Mac = mac;
        }

        public String getEstatus() {
            return Estatus;
        }

        public void setEstatus(String Estatus) {
            this.Estatus = Estatus;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }
    }
}
