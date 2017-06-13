package cn.harrysean.veisky;
import java.util.ArrayList;
import java.util.HashMap;
import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
public class AdapterCartList extends BaseAdapter {
	private FinalBitmap fb;
	ArrayList<HashMap<String, Object>> data;
	private LayoutInflater inflater; // 布局填充器，Android的内置服务，作用：使用xml文件来生成对应的view对象
	public AdapterCartList(Context context, ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		// 得到布局填充服务
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fb = FinalBitmap.create(context);
		fb.configLoadingImage(R.drawable.pull_down);
	}
	public int getCount() {
		return data.size();
	}
	public Object getItem(int arg0) {
		return data.get(arg0);
	}
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public int getItemViewType(int position) {
		if (data.get(position).get("isshop").equals(true)) {
			return 1;
		} else {
			return 0;
		}
	}
	// 取得代表条目的view对象
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView lvimg = null;
		TextView lvname = null,lvshopname = null,lvprice = null;
		EditText lvcount = null;
		int type = getItemViewType(position);
		HashMap<String, Object> d = data.get(position);
		if (convertView == null) {
			ViewCache cache = new ViewCache();
			switch (type) {
				case 0 :
					convertView = inflater.inflate(R.layout.lstcartlist, null);
					lvname = (TextView) convertView.findViewById(R.id.cart_goodsname);
					lvprice = (TextView) convertView.findViewById(R.id.cart_price);
					lvcount = (EditText) convertView.findViewById(R.id.cart_goodsnum);
					lvimg = (ImageView) convertView.findViewById(R.id.cart_image);
					cache.lvname = lvname;
					cache.lvprice = lvprice;
					cache.lvimg = lvimg;
					cache.lvcount = lvcount;
					break;
				case 1 :
					convertView = inflater.inflate(R.layout.lstcartlistshop, null);
					lvshopname = (TextView) convertView.findViewById(R.id.cart_shopname);
					cache.lvshopname = lvshopname;
					break;
			}
			convertView.setTag(cache);
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			switch (type) {
				case 0 :
					lvname = cache.lvname;
					lvprice = cache.lvprice;
					lvimg = cache.lvimg;
					lvcount = cache.lvcount;
					break;
				case 1 :
					lvshopname = cache.lvshopname;
					break;
			}
		}
		switch (type) {
			case 0 :
				lvname.setText(d.get("name").toString());
				lvprice.setText("￥"+d.get("price").toString());
				lvcount.setText(d.get("count").toString());
				fb.display(lvimg, d.get("img").toString());
				break;
			case 1 :
				lvshopname.setText(d.get("shopname").toString());
				break;
		}
		return convertView;
	}
	private final class ViewCache {
		public TextView lvname;
		public TextView lvprice;
		public ImageView lvimg;
		public TextView lvshopname;
		public EditText lvcount;
		
	}
}
