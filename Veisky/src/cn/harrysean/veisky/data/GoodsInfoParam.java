package cn.harrysean.veisky.data;

public class GoodsInfoParam extends Param{
	public GoodsInfoParam(pTypeEnum type) {
		super(type);
	}
	public void setId(int id){
		map.put("id",Integer.valueOf(id).toString());
	}

}
