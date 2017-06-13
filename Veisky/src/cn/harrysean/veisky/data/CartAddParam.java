package cn.harrysean.veisky.data;

public class CartAddParam extends Param{
	public CartAddParam(pTypeEnum type) {
		super(type);
	}
	public void setUid(int uid){
		map.put("uid",uid);
	}
	public void setGid(int gid){
		map.put("gid",gid);
	}
	public void setCount(int count){
		map.put("count",count);
	}
	

}
