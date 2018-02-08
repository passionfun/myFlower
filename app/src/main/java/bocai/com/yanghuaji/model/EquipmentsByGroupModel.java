package bocai.com.yanghuaji.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/11/27.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentsByGroupModel {

    /**
     * GroupName : 办公室
     * List : [{"Id":"2","EquipName":"WG222+111"},{"Id":"1","EquipName":"WG222+333"}]
     */

    private String GroupName;
    private java.util.List<EquipmentCard> List;
    private java.util.List<EquipmentCard> NoGroupList;

    public java.util.List<EquipmentCard> getNoGroupList() {
        return NoGroupList;
    }

    public void setNoGroupList(java.util.List<EquipmentCard> noGroupList) {
        NoGroupList = noGroupList;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public java.util.List<EquipmentCard> getList() {
        return List;
    }

    public void setList(java.util.List<EquipmentCard> list) {
        List = list;
    }
}
