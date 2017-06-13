package cn.harrysean.veisky.data;

import java.util.HashMap;
import net.tsz.afinal.http.AjaxParams;


public abstract class Param {
	private final String url="http://harrysean.cn/veisky/";
	//private final String url="http://192.168.128.143/veisky/";
	public pTypeEnum pType;
	protected HashMap<String, Object> map = new HashMap<String, Object>();
	public enum pTypeEnum{
		shop_show,goods_show,goods_info,user_login,cart_add,cart_show,order_add,order_show
	}
	Param(pTypeEnum type){
		pType=type;
	}
	public String getUrl(){
		switch(pType){
		case shop_show:
			return url+"api.php?type=shop_show";
		case goods_show:
			return url+"api.php?type=goods_show";
		case goods_info:
			return url+"api.php?type=goods_info";
		case user_login:
			return url+"api.php?type=user_login";
		case cart_add:
			return url+"api.php?type=cart_add";
		case cart_show:
			return url+"api.php?type=cart_show";
		case order_add:
			return url+"api.php?type=order_add";
		case order_show:
			return url+"api.php?type=order_show"; 
		}
		return "";
	}
	public AjaxParams getParams(){
		AjaxParams params = new AjaxParams();
		switch(pType){
		case goods_show:
			params.put("sid", (String)map.get("sid"));
			break;
		case goods_info:	
			params.put("id", (String)map.get("id"));
			break;
		case user_login:	
			params.put("password", map.get("password").toString());
			params.put("email", map.get("email").toString());
			break;
		case cart_add:
			params.put("uid", map.get("uid").toString());
			params.put("gid", map.get("gid").toString());
			params.put("count", map.get("count").toString());
			break;
		case cart_show:
			params.put("uid", map.get("uid").toString());
			break;
		case order_add:
			params.put("uid", map.get("uid").toString());
			break;
		case order_show:
			params.put("uid", map.get("uid").toString());
			break;
			
		
		default:
			return null;

		}
		return params;
	}
}
