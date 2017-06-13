package cn.harrysean.veisky.data;

public class OrderShowParam extends Param{
	public OrderShowParam(pTypeEnum type) {
		super(type);
	}
	public void setUid(int uid){
		map.put("uid",uid);
	}
	
	

}
