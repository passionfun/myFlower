package bocai.com.yanghuajien.updateVersion.manager.fileload;

/**
 *
 * Created by shc on 2018/1/4.
 */
public class FileLoadingBean {
    /**
     * 文件大小
     */
    long total;
    /**
     * 已下载大小
     */
    long progress;

    public long getProgress() {
        return progress;
    }

    public long getTotal() {
        return total;
    }

    public FileLoadingBean(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}
