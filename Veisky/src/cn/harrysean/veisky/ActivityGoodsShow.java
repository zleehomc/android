package cn.harrysean.veisky;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.harrysean.veisky.data.GoodsShowParam;
import cn.harrysean.veisky.data.VskCallback;
import cn.harrysean.veisky.data.VskClient;
import cn.harrysean.veisky.data.Param.pTypeEnum;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class ActivityGoodsShow extends FinalActivity {
	@ViewInject(id = R.id.lvGoods)
	ListView lv1;
	@ViewInject(id = R.id.title)
	TextView title;
	int sid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodsshow);
		Intent i=getIntent();
		sid=i.getIntExtra("sid", 0);
		title.setText(i.getStringExtra("shopname"));
		VskClient clt = new VskClient(this);
		GoodsShowParam param = new GoodsShowParam(pTypeEnum.goods_show);
		param.setSid(sid);
		//Toast.makeText(getApplicationContext(), Integer.valueOf(sid).toString(),Toast.LENGTH_SHORT).show();
		clt.sendRequest(param, new VskCallback() {
			@Override
			public void onSuccess(JSONArray response) {
				setInList(response);
			}

			@Override
			public void onError(String errCode, String errMessage) {

			}
		});
		
		
	}
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	protected void setInList(JSONArray jArray) {
		// 生成动态数组，加入数据
		
		try {
			JSONObject json_data = null;
			json_data = jArray.getJSONObject(0);
			for (int i = 0; i < jArray.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				json_data = jArray.getJSONObject(i);
				map.put("name", json_data.getString("name"));
				map.put("price", "￥"+json_data.getString("price"));
				map.put("id", json_data.getString("id"));
				map.put("img", json_data.getString("img"));
				listItem.add(map);
			}
		} catch (JSONException e1) {
			
		}
		AdapterGoodsShow listItemAdapter = new AdapterGoodsShow(ActivityGoodsShow.this, listItem); 

		lv1.setAdapter(listItemAdapter);
		lv1.setOnItemClickListener(new OnItemClickListener() {  
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				//Toast.makeText(getApplicationContext(), listItem.get(arg2).get("ItemName").toString(),Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("id", Integer.valueOf(listItem.get(arg2).get("id").toString()));
				intent.putExtra("sid", sid);
				intent.putExtra("shopname", title.getText().toString());
				//intent.putExtra("sid", "1");
	            intent.setClass(ActivityGoodsShow.this, ActivityGoodsInfo.class);
	            startActivity(intent);
			}  
    });  
	}

}
