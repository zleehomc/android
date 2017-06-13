package Face.JavaBean;

import java.io.Serializable;

/**
 * Created by hyc on 2016/3/30.
 */
public class myUserDetail implements Serializable {

    /**
     * UserId : 1
     * UserName : 寒亦唱
     * Password : 111111
     * email : 514300914@qq.com
     * t_Status : 1
     */

    private String UserId=null;
    private String UserName=null;
    private String Password=null;
    private String email=null;
    private String t_Status=null;

    public myUserDetail(String userId,String userName,String email,String t_Status) {
        this.UserId=userId;
        this.UserName=userName;
        this.email=email;
        this.t_Status=t_Status;
    }
    public myUserDetail(){
    }


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getT_Status() {
        return t_Status;
    }

    public void setT_Status(String t_Status) {
        this.t_Status = t_Status;
    }
}
