package cn.harrysean.veisky;
import java.util.ArrayList;
import java.util.HashMap;
import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class AdapterShopShow extends BaseAdapter {
	private FinalBitmap fb;
	ArrayList<HashMap<String, Object>> data;
	private LayoutInflater inflater; // 布局填充器，Android的内置服务，作用：使用xml文件来生成对应的view对象
	public AdapterShopShow(Context context, ArrayList<HashMap<String, Object>> data) {
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
	// 取得代表条目的view对象
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView lvimg = null;
		TextView lvname = null;
		TextView lvmobile = null;
		HashMap<String, Object> d = data.get(position);
		if (convertView == null) {
			ViewCache cache = new ViewCache();
			convertView = inflater.inflate(R.layout.lstshoplist, null);
			lvname = (TextView) convertView.findViewById(R.id.lv_name);
			lvmobile = (TextView) convertView.findViewById(R.id.lv_mobile);
			lvimg = (ImageView) convertView.findViewById(R.id.shop_image);
			cache.lvname = lvname;
			cache.lvmobile = lvmobile;
			cache.lvimg = lvimg;
			convertView.setTag(cache);
		} else {
			ViewCache cache = (ViewCache) convertView.getTag();
			lvname = cache.lvname;
			lvmobile = cache.lvmobile;
			lvimg = cache.lvimg;
		}
		lvname.setText(d.get("name").toString());
		lvmobile.setText(d.get("mobile").toString());
		fb.display(lvimg, d.get("img").toString());
		return convertView;
	}
	private final class ViewCache {
		public TextView lvname;
		public TextView lvmobile;
		public ImageView lvimg;
	}
}
