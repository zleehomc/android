package cn.harrysean.veisky.data;

public class GoodsShowParam extends Param{
	
	public GoodsShowParam(pTypeEnum type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	
	public void setSid(int sid){
		map.put("sid",Integer.valueOf(sid).toString());
	}
}
