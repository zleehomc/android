package cn.harrysean.veisky;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

public class Config extends Application {
	private ArrayList<HashMap<String, Object>> cart = new ArrayList<HashMap<String, Object>>();
	
	private boolean UserLogin;
	private int UserId;
	public Config(){
		UserLogin=false;
	}
	public boolean isUserLogin(){
		return UserLogin;
	}
	public void setUserLogin(boolean b,int Id){
		UserLogin=b;
		UserId=Id;
	}
	public int getUserId(){
		return UserId;
	}
	public void clearcart(){
		cart.clear();
	}
	
	public void addToCart(int id,int sid,String count,String sname,String gname,double d,String img){
		HashMap<String, Object> map = new HashMap<String, Object>();
		int cartinsert=0;
		for(int i=0;i<cart.size();i++){
			if(cart.get(i).get("isshop").equals(true)){
				if(cart.get(i).get("sid").equals(sid)){
					cartinsert=i+1;
					break;
				}
				
			}
		}
		if(cartinsert==0){
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("isshop", true);
			map2.put("shopname", sname);
			map2.put("sid", sid);
			cart.add(map2);
			cartinsert=cart.size();
		}
		
		map.put("isshop", false);
		map.put("id", id);
		map.put("count", count);
		map.put("sid", sid);
		map.put("name", gname);
		map.put("price", d);
		map.put("img", img);
		cart.add(cartinsert,map);
	}
	public boolean isCartEmpty(){
		return cart.isEmpty();
	}
	public ArrayList<HashMap<String, Object>> getCart(){
		return cart;
	}
	
}
