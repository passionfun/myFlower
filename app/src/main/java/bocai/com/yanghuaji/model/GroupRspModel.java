package bocai.com.yanghuaji.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/11/25.
 * 邮箱 yuanfei221@126.com
 */

public class GroupRspModel {


    /**
     * List : [{"Id":"2","GroupName":"阳台","Equipment":[{"Id":"3","EquipName":"WG222+2222"}]},{"Id":"1","GroupName":"办公室","Equipment":[{"Id":"2","EquipName":"WG222+111"},{"Id":"1","EquipName":"WG222+333"}]}]
     * CountPage : 1
     * Count : 3
     */

    private int CountPage;
    private int Count;
    private java.util.List<ListBean> List;

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

    public static class ListBean {
        /**
         * Id : 2
         * GroupName : 阳台
         * Equipment : [{"Id":"3","EquipName":"WG222+2222"}]
         */

        private String Id;
        private String GroupName;
        private java.util.List<EquipmentBean> Equipment;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public List<EquipmentBean> getEquipment() {
            return Equipment;
        }

        public void setEquipment(List<EquipmentBean> Equipment) {
            this.Equipment = Equipment;
        }

        public static class EquipmentBean {
            /**
             * Id : 3
             * EquipName : WG222+2222
             */

            private String Id;
            private String EquipName;

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
        }
    }
}
