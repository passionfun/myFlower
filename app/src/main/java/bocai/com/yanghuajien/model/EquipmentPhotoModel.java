package bocai.com.yanghuajien.model;

import java.io.Serializable;

/**
 * 作者 yuanfei on 2017/12/1.
 * 邮箱 yuanfei221@126.com
 */

public class EquipmentPhotoModel implements Serializable{


    /**
     * "Video": "http://121.41.128.239:8082/yhj/web/upload/2018/01/11/15156580256172mlgxk.mp3"
     * Photo : http://121.41.128.239:8082/yhj/web/upload/2017/11/27/15117835837686riw8.png
     */

    private String Photo;
    private String Video;


    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }
}
