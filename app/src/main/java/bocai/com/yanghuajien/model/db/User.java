package bocai.com.yanghuajien.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 作者 yuanfei on 2017/11/20.
 * 邮箱 yuanfei221@126.com
 */
@Table(database = AppDatabase.class)
public class User extends BaseModel{
    // 主键
    @PrimaryKey
    private String Id;
    @Column
    private String Token;
    @Column
    private String Phone;
    @Column
    private String NickName;
    @Column
    private String Sex;
    @Column
    private String Birthday;
    @Column
    private String RelativePath;
    @Column
    private String SmallThumbnail;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getRelativePath() {
        return RelativePath;
    }

    public void setRelativePath(String relativePath) {
        RelativePath = relativePath;
    }

    public String getSmallThumbnail() {
        return SmallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        SmallThumbnail = smallThumbnail;
    }
}
