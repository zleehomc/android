package cn.harrysean.veisky.data;

public class OrderAddParam extends Param{
	public OrderAddParam(pTypeEnum type) {
		super(type);
	}
	public void setUid(int uid){
		map.put("uid",uid);
	}
	
	

}
