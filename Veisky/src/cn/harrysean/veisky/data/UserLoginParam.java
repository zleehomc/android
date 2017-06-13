package cn.harrysean.veisky.data;

public class UserLoginParam extends Param{
	public UserLoginParam(pTypeEnum type) {
		super(type);
	}
	public void setEmail(String email){
		map.put("email",email);
	}
	public void setPassword(String password){
		map.put("password",password);
	}

}
