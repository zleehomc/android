package cn.harrysean.veisky;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.harrysean.veisky.data.OrderShowParam;
import cn.harrysean.veisky.data.VskCallback;
import cn.harrysean.veisky.data.VskClient;
import cn.harrysean.veisky.data.Param.pTypeEnum;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class ActivityBill extends FinalActivity  {
	@ViewInject(id = R.id.billshopname)
	TextView billshopname;
	@ViewInject(id = R.id.billtotal)
	TextView billtotal;
	@ViewInject(id = R.id.billaddress)
	EditText billaddress;
	@ViewInject(id = R.id.billremark)
	TextView billremark;
	@ViewInject(id = R.id.lvbillgoods)
	ListView lvbillgoods;
	@ViewInject(id = R.id.lvBilltotal)
	ListView lvBilltotal;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.billpage);
		VskClient clt = new VskClient(this);
		OrderShowParam param = new OrderShowParam(pTypeEnum.order_show);
		param.setUid(((Config)getApplicationContext()).getUserId());
		clt.sendRequest(param, new VskCallback() {
			@Override
			public void onSuccess(JSONArray response) {
				//Log.v("tip", "你是谁！");
				setInList(response);
	
			}

			@Override
			public void onError(String errCode, String errMessage) {
			//	Log.v("tip", "你是谁2！");
			}
		});
		
		
		
	}
	
	private ArrayList<HashMap<String, Object>> list_order = new ArrayList<HashMap<String, Object>>();
	protected void setInList(JSONArray jArray) {
		// 生成动态数组，加入数据
		
		try {
		//	Log.v("tip", "你是谁3！");
			JSONObject json_data = null;
			JSONObject json_data2 = null;
			for (int i = 0; i < jArray.length(); i++) {
				
				json_data = jArray.getJSONObject(i);
				JSONArray jArray2=json_data.getJSONArray("data");
				for(int j=0;j<jArray2.length();j++){
					 json_data2 = jArray2.getJSONObject(j);
					Log.v("tip", json_data2.getString("name"));
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("number", json_data.getString("num"));
					map.put("shopname", json_data.getString("shopname"));
					map.put("sid", json_data.getInt("sid"));
					map.put("gid", json_data2.getInt("gid"));
					map.put("price", "￥"+json_data2.getDouble("price"));
					map.put("freight", json_data2.getDouble("freight"));
					map.put("gname", json_data2.getString("name"));
					map.put("count", json_data2.getInt("count")+"个 ");
					map.put("state", json_data.getString("state"));
					list_order.add(map);
				
				
				}
				
			}
		} catch (JSONException e1) {
			Log.v("tip", "你是谁4！");
			// Toast.makeText(getBaseContext(), "No City Found"
			// ,Toast.LENGTH_LONG).show();
		}
		// 生成适配器的Item和动态数组对应的元素
		final SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				list_order,// 数据源
				R.layout.myorderlist, new String[] {  "shopname" ,"price","gname","count","state"},
				new int[] { R.id.billshopname,R.id.billprice,R.id.bill_goodsname,R.id.bill_goodsnum,R.id.state});

		// 添加并且显示
		lvbillgoods.setAdapter(listItemAdapter);
		 
	}
	
}
		
		
		
		
		
		
		
		
		
		
		
		
		
	

