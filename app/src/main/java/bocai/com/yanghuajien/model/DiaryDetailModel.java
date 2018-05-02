package bocai.com.yanghuajien.model;

import java.util.List;

/**
 * 作者 yuanfei on 2017/11/30.
 * 邮箱 yuanfei221@126.com
 */

public class DiaryDetailModel {


    /**
     * Photos : ["http://121.41.128.239:8082/yhj/web/upload/2017/11/13/201711131047271NS967731.png","http://121.41.128.239:8082/yhj/web/upload/2017/11/13/20171113105950Gh4i5291.png"]
     * Content : 这学期我突然迷上了种花草,参加了学校的苗圃兴趣小组
     * Location : 杭州.联合大厦
     * Timeline : 1510572950
     */

    private String Content;
    private String Location;
    private String Timeline;
    private List<String> Photos;

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getTimeline() {
        return Timeline;
    }

    public void setTimeline(String Timeline) {
        this.Timeline = Timeline;
    }

    public List<String> getPhotos() {
        return Photos;
    }

    public void setPhotos(List<String> Photos) {
        this.Photos = Photos;
    }
}
