package cn.harrysean.veisky.data;



public class CartShowParam extends Param{
	public CartShowParam(pTypeEnum type) {
		super(type);
	}
	public void setUid(int uid){
		map.put("uid",Integer.valueOf(uid).toString());
	}
	
	

}
