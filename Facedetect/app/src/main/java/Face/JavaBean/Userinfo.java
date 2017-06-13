package Face.JavaBean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by hyc on 2016/3/26.
 */
public class Userinfo implements Serializable{
    //用户明、考考、照片id、座位号
    private String username;
    private String assid;
    private Bitmap bitmap;
    private String seatnumb;
    private String userid;
    private String ass_status;

    /**
     * Constructs a new instance of {@code Object}.
     */
    public Userinfo(String userid,String username, String assid, Bitmap photoId,String seatnumb,String ass_status) {
        this.userid=userid;
        this.username=username;
        this.assid=assid;
        this.bitmap=photoId;
        this.seatnumb=seatnumb;
        this.ass_status=ass_status;
    }

    public void setuserid(String userid){this.userid=userid;}
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAssidid(String examid) {
        this.assid = examid;
    }
    public void setPhoto(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void setSeatNumb(String seatnumb){
        this.seatnumb=seatnumb;
    }
    public void setAss_status(String ass_status){
        this.ass_status=ass_status;
    }


    public String getUserid(){return userid;}
    public String getUsername() {
        return username;
    }
    public String getAssid() {
        return assid;
    }
    public Bitmap getPhoto() {
        return bitmap;
    }
    public String getSeatnumb(){
        return seatnumb;
    }
    public String getAss_status(){
        return ass_status;
    }





}
