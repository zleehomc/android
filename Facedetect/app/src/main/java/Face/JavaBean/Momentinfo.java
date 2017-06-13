package Face.JavaBean;

import java.io.Serializable;

/**
 * Created by hyc on 2016/4/24.
 */
public class Momentinfo implements Serializable{

	/**
	 * Moment_Id : 1
	 * Moment_content : 今天有点饿了
	 * User_Id : 33
	 * UserName : 回忆专用小马甲
	 * Moment_send_time : 2016-04-19 18:37:41
	 */

	private String Moment_Id;
	private String Moment_content;
	private String UserName;
	private String Moment_send_time;
	private String User_Id;

	public String getMoment_Id() {
		return Moment_Id;
	}



	public void setMoment_Id(String Moment_Id) {
		this.Moment_Id = Moment_Id;
	}

	public String getUser_Id(){return  User_Id;}

	public void setUser_Id(String User_Id) {
		this.User_Id = User_Id;
	}


	public String getMoment_content() {
		return Moment_content;
	}

	public void setMoment_content(String Moment_content) {
		this.Moment_content = Moment_content;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String UserName) {
		this.UserName = UserName;
	}

	public String getMoment_send_time() {
		return Moment_send_time;
	}

	public void setMoment_send_time(String Moment_send_time) {
		this.Moment_send_time = Moment_send_time;
	}
}
