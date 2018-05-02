package bocai.com.yanghuajien.model;

/**
 *
 * Created by shc on 2018/1/4.
 */

public class VersionInfoModel {

    /**
     * Version : 1.0.1
     * Title : 更新了更新了
     * Url : http://121.41.128.239:8082/yhj/web/apk
     * Forceupdating : false
     * Timeline : 1514267754
     */

    private String Version;
    private String Title;
    private String Url;
    private boolean Forceupdating;
    private String Timeline;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public boolean isForceupdating() {
        return Forceupdating;
    }

    public void setForceupdating(boolean Forceupdating) {
        this.Forceupdating = Forceupdating;
    }

    public String getTimeline() {
        return Timeline;
    }

    public void setTimeline(String Timeline) {
        this.Timeline = Timeline;
    }
}
