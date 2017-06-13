package cn.harrysean.veisky;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.harrysean.veisky.R;
import cn.harrysean.veisky.data.CartShowParam;
import cn.harrysean.veisky.data.OrderAddParam;
import cn.harrysean.veisky.data.Param.pTypeEnum;
import cn.harrysean.veisky.data.ShopGetParam;
import cn.harrysean.veisky.data.VskCallback;
import cn.harrysean.veisky.data.VskClient;
import cn.harrysean.veisky.widget.MsgListView;
import cn.harrysean.veisky.widget.MsgListView.OnRefreshListener;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class VskActivity extends FinalActivity {

	public static String url = "http://harrysean.cn/veisky/test.php";
	@ViewInject(id = R.id.btnmycount)
	Button btnmycount;
	@ViewInject(id = R.id.lvShop)
	MsgListView lstShop;
	@ViewInject(id = R.id.lvcart)
	MsgListView lstCart;
	@ViewInject(id = R.id.btn_ord)
	Button btn_ord;
	@ViewInject(id = R.id.btngotobuy)
	Button btngotobuy;
	@ViewInject(id = R.id.tabhost)
	TabHost tabs;
	@ViewInject(id = R.id.lcartempty)
	RelativeLayout lcartempty;
	@ViewInject(id = R.id.lcartnotempty)
	LinearLayout lcartnotempty;
	@ViewInject(id = R.id.checkAll)
	CheckBox checkAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vsk);
		setTitle("味思客客户端");

		/* 设置TAB选项卡 */
		tabs.setup();
		tabs.addTab(tabs.newTabSpec("tab1").setIndicator("首页", getResources().getDrawable(R.drawable.tab1))
				.setContent(R.id.tab1));
		tabs.addTab(tabs.newTabSpec("tab2").setIndicator("订餐", getResources().getDrawable(R.drawable.tab2))
				.setContent(R.id.tab2));
		tabs.addTab(tabs.newTabSpec("tab3").setIndicator("购物车", getResources().getDrawable(R.drawable.tab3))
				.setContent(R.id.tab3));
		tabs.addTab(tabs.newTabSpec("tab4").setIndicator("我的订餐", getResources().getDrawable(R.drawable.tab4))
				.setContent(R.id.tab4));
		tabs.setCurrentTab(0);
		/* 设置结束 */

		/* 设置按钮监听 */
		btn_ord.setOnClickListener(new ButtonClickListener());
		btnmycount.setOnClickListener(new ButtonClickListener());
		btngotobuy.setOnClickListener(new ButtonClickListener());

		/* TAB选项卡监听 */
		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("tab3") || tabId.equals("tab4")) {

					boolean isUserLogin = ((Config) getApplicationContext()).isUserLogin();
					if (!isUserLogin) {
						tabs.setCurrentTab(0);
						Intent intent = new Intent();
						intent.setClass(VskActivity.this, ActivityUserLogin.class);
						startActivity(intent);
					} else {
						if (tabId.equals("tab3")) {
							/* 获取购物车列表 */
							getCart();
							setInCartList();
							refreshCartState();
						}
					}
				}
			}
		});

		/* 获取店铺列表 */
		getShopList();
		/* 设置下拉刷新监听 */
		lstShop.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getShopList();
			}
		});

		/* 设置购物车下拉刷新监听 */
		lstCart.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				lstCart.clearTextFilter();
				getCart();
				setInCartList();
			}
		});

	}
	private void getShopList() {
		VskClient clt = new VskClient(this);
		ShopGetParam param = new ShopGetParam(pTypeEnum.shop_show);
		clt.sendRequest(param, new VskCallback() {
			@Override
			public void onSuccess(JSONArray response) {
				setInShopList(response);
			}

			@Override
			public void onError(String errCode, String errMessage) {

			}
		});
	}
	private void refreshCartState() {
		if (((Config) getApplicationContext()).isCartEmpty()) {
			lcartempty.setVisibility(View.VISIBLE);
			lcartnotempty.setVisibility(View.GONE);
			checkAll.setVisibility(View.GONE);
		} else {
			lcartnotempty.setVisibility(View.VISIBLE);
			lcartempty.setVisibility(View.GONE);
			checkAll.setVisibility(View.VISIBLE);
		}
	}
	private final class ButtonClickListener implements View.OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btnmycount :
					Intent intent = new Intent();
					intent.setClass(VskActivity.this, ActivityMyCount.class);
					startActivity(intent);
					break;
				case R.id.btngotobuy :
					tabs.setCurrentTab(1);
					break;
				case R.id.btn_ord :
					new AlertDialog.Builder((VskActivity.this)).setTitle("结算").setMessage("是否结算？")
							.setPositiveButton("是", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Intent intent = new Intent();
									intent.setClass(VskActivity.this, ActivityBill.class);
									startActivity(intent);

									VskClient clt2 = new VskClient(VskActivity.this);
									OrderAddParam param = new OrderAddParam(pTypeEnum.order_add);
									param.setUid(((Config) getApplicationContext()).getUserId());
									clt2.sendRequest(param, new VskCallback() {
										@Override
										public void onSuccess(JSONArray response) {
											JSONObject jd;
											try {
												jd = (JSONObject) response.getJSONObject(0);
												if (jd.getInt("success") == 1) {
													Toast.makeText(getApplicationContext(), jd.getString("info"),
															Toast.LENGTH_SHORT).show();
													((Config) getApplicationContext()).clearcart();
													lstCart.clearTextFilter();
													setInCartList();
												} else {
													Toast.makeText(getApplicationContext(), jd.getString("info"),
															Toast.LENGTH_SHORT).show();

												}
											} catch (JSONException e) {
												e.printStackTrace();
											}

										}

										@Override
										public void onError(String errCode, String errMessage) {
											Toast.makeText(getApplicationContext(), "你tm妹的又错了！", Toast.LENGTH_SHORT)
													.show();
										}
									});
								}
							}).setNegativeButton("否", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
								}
							}).show();
					break;
			}
		}
	}
	ArrayList<HashMap<String, Object>> shoplistdata = new ArrayList<HashMap<String, Object>>();
	private void setInShopList(JSONArray jArray) {
		shoplistdata.clear();
		try {
			JSONObject json_data = null;

			for (int i = 0; i < jArray.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				json_data = jArray.getJSONObject(i);
				map.put("name", json_data.getString("name"));
				map.put("mobile", json_data.getString("mobile"));
				map.put("img", json_data.getString("img"));
				map.put("sid", json_data.getInt("id"));
				shoplistdata.add(map);
			}
		} catch (JSONException e1) {
			lstShop.onRefreshComplete();
		}
		AdapterShopShow listItemAdapter = new AdapterShopShow(VskActivity.this, shoplistdata); 

		// 添加并且显示
		lstShop.setAdapter(listItemAdapter);
		lstShop.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int id = arg2 - 1;
				Intent intent = new Intent();
				intent.putExtra("sid", Integer.valueOf(shoplistdata.get(id).get("sid").toString()));
				intent.putExtra("shopname", shoplistdata.get(id).get("ItemName").toString());
				intent.setClass(VskActivity.this, ActivityGoodsShow.class);
				startActivity(intent);
			}
		});
		lstShop.onRefreshComplete();
	}

	public void getCart() {
		VskClient clt1 = new VskClient(this);
		CartShowParam param1 = new CartShowParam(pTypeEnum.cart_show);
		param1.setUid(((Config) getApplicationContext()).getUserId());
		clt1.sendRequest(param1, new VskCallback() {
			@Override
			public void onSuccess(JSONArray response) {
				try {
					((Config) getApplicationContext()).clearcart();
					JSONObject json_data = null;
					for (int i = 0; i < response.length(); i++) {
						json_data = response.getJSONObject(i);
						((Config) getApplicationContext()).addToCart(json_data.getInt("id"),json_data.getInt("sid"),
								json_data.getString("count"), json_data.getString("shopname"),
								json_data.getString("name"), json_data.getDouble("price"),
								json_data.getString("img"));
					}
					refreshCartState();
				} catch (JSONException e1) {

				}

				setInCartList();

			}

			@Override
			public void onError(String errCode, String errMessage) {

			}
		});

	}
	private void setInCartList() {
		ArrayList<HashMap<String, Object>> cart = ((Config) getApplicationContext()).getCart();
		AdapterCartList listItemAdapter = new AdapterCartList(VskActivity.this, cart); 
		lstCart.setAdapter(listItemAdapter);
		lstCart.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			}
		});
		lstCart.onRefreshComplete();
	}
	@Override
	/* 按键菜单功能 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wsk, menu);
		return true;
	}

}
