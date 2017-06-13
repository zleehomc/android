package cn.harrysean.veisky;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.harrysean.veisky.data.CartAddParam;
import cn.harrysean.veisky.data.GoodsInfoParam;
import cn.harrysean.veisky.data.VskCallback;
import cn.harrysean.veisky.data.VskClient;
import cn.harrysean.veisky.data.Param.pTypeEnum;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

public class ActivityGoodsInfo extends FinalActivity {
	private FinalBitmap fb;
	@ViewInject(id = R.id.goods_shoptitle)
	TextView goods_shoptitle;
	@ViewInject(id = R.id.goodsname)
	TextView gname;
	@ViewInject(id = R.id.goodsprice)
	TextView gprice;
	@ViewInject(id = R.id.goodsstock)
	TextView gstock;
	@ViewInject(id = R.id.introduce)
	TextView gintroduce;
	@ViewInject(id = R.id.goodscount)
	EditText edgc;
	@ViewInject(id = R.id.btnaddtocart)
	Button btnatc;
	@ViewInject(id = R.id.gallery1)
	ImageView gallery1;
	JSONObject json_data = null;
	int id,sid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodsinfo);
		Intent i=getIntent();
		id=i.getIntExtra("id", 0);
		sid=i.getIntExtra("sid", 0);
		goods_shoptitle.setText(i.getStringExtra("shopname"));
		VskClient clt = new VskClient(this);
		GoodsInfoParam param1= new GoodsInfoParam(pTypeEnum.goods_info);
		param1.setId(id);
		btnatc.setOnClickListener(new ButtonClickListener());
		fb = FinalBitmap.create(this);
		fb.configLoadingImage(R.drawable.pull_down);
		//Toast.makeText(getApplicationContext(), Integer.valueOf(sid).toString(),Toast.LENGTH_SHORT).show();
		clt.sendRequest(param1, new VskCallback() {
			@Override
			public void onSuccess(JSONArray response) {
				try {
					
					
					json_data = response.getJSONObject(0);
					fb.display(gallery1,json_data.get("img").toString());
					
					gname.setText(json_data.getString("name"));
					gprice.setText("$"+json_data.getString("price"));
					gstock.setText(json_data.getString("stock"));
					if(json_data.getString("introduce").equals("null")){
						gintroduce.setText("尚未有简介");
					}else{
						gintroduce.setText(json_data.getString("introduce"));
					}
					
				} catch (JSONException e1) {
				}
				
			}

			@Override
			public void onError(String errCode, String errMessage) {
				
			}
		});
		
		
		
	}
	private final class ButtonClickListener implements View.OnClickListener {
		
		public void onClick(View v) {
			
			switch (v.getId()) {
				case R.id.btnaddtocart:
					VskClient clt = new VskClient(ActivityGoodsInfo.this);
					CartAddParam param = new CartAddParam(pTypeEnum.cart_add);
					
					param.setUid(((Config)getApplicationContext()).getUserId());
					param.setGid(id);
					param.setCount(Integer.valueOf(edgc.getText().toString()));
					//Toast.makeText(getApplicationContext(), Integer.valueOf(sid).toString(),Toast.LENGTH_SHORT).show();
					clt.sendRequest(param, new VskCallback() {
						@Override
						public void onSuccess(JSONArray response) {
							JSONObject jd;
							try {
								jd = (JSONObject)response.getJSONObject(0);
								if(jd.getInt("success")==1){
									Toast.makeText(getApplicationContext(),"增加到购物车成功",Toast.LENGTH_SHORT).show();
									((Config)getApplicationContext()).addToCart(
											id,sid,edgc.getText().toString(),
											goods_shoptitle.getText().toString(), 
											json_data.getString("name"), 
											json_data.getDouble("price"), 
											json_data.getString("img"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
						}
						
						@Override
						public void onError(String errCode, String errMessage) {

						}
					});
					break;
			}
		}
	}
}
