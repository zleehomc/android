package Face.Adpater;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.android.hyc.hyc_final.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Face.CircleImageView;
import Face.JavaBean.Momentinfo;

/**
 * Created by hyc on 2016/4/19.
 */
public class MomentRecyclerViewAdpater extends RecyclerView.Adapter<MomentRecyclerViewAdpater.MomentViewHolder> {

	private List<Momentinfo> momentinfos;
	private Context context;
	private Boolean isScrollToTop;
	private RequestQueue mQueue;

	static class MomentViewHolder extends RecyclerView.ViewHolder {
		CircleImageView userimageview;
		TextView username_textview;
		TextView passtime_textview;
		TextView content_textview;
		TextView device_textview;
		Button share_button;
		Button commit_button;
		Button praise_button;
		public MomentViewHolder(final View itemView) {
			super(itemView);
			userimageview=(CircleImageView)itemView.findViewById(R.id.userphoto_civ);
			username_textview=(TextView)itemView.findViewById(R.id.username_mc_tv);
			passtime_textview=(TextView)itemView.findViewById(R.id.passtime_mc_tv);
			content_textview=(TextView)itemView.findViewById(R.id.content_mc_tv);
			device_textview=(TextView)itemView.findViewById(R.id.device_mc_tv);
			share_button = (Button) itemView.findViewById(R.id.button_mc_share);
			commit_button = (Button) itemView.findViewById(R.id.button__mc_commit);
			praise_button=(Button)itemView.findViewById(R.id.button_mc_praise);
		}
	}
	public MomentRecyclerViewAdpater(List<Momentinfo> momentinfos, Context context, boolean isScrollToTop) {
		this.momentinfos = momentinfos;
		this.context=context;
		this.isScrollToTop=isScrollToTop;
	}
	@Override
	public MomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v= LayoutInflater.from(context).inflate(R.layout.event_item,parent,false);
		MomentViewHolder momentViewHolder=new MomentViewHolder(v);
		return momentViewHolder;
	}

	@Override
	public void onBindViewHolder(MomentViewHolder holder, int position) {
		final int j=position;
		mQueue= Volley.newRequestQueue(context);
		ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.userimageview,
				     R.drawable.default_image, R.drawable.failed_image);
		imageLoader.get(context.getResources().getString(R.string.baidu_bos_host)+"/pic/"+momentinfos.get(position).getUser_Id()+".jpg"
				,listener, 60, 60);
		Log.v("image_url",context.getResources().getString(R.string.ipwireless)+"/appp/pic/"+momentinfos.get(position).getUser_Id()+".jpg");

		holder.username_textview.setText(momentinfos.get(position).getUserName());
		Log.v("tag",momentinfos.get(position).getUserName());
		holder.content_textview.setText(momentinfos.get(position).getMoment_content());
		holder.device_textview.setText("来自"+Build.MODEL+"客户端");
        holder.passtime_textview.setText(get_between_time(momentinfos.get(position).getMoment_send_time()));
		if (isScrollToTop) {//根据滑动方向设置动画
			holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_in_top_anim));
		} else {
			holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_in_bottom_anim));
		}
	}

	@Override
	public int getItemCount() {
		return momentinfos.size();
	}
	public void onViewDetachedFromWindow(MomentViewHolder holder) {
		super.onViewDetachedFromWindow(holder);
		holder.itemView.clearAnimation();
	}

    public String get_between_time(String passtime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Date current_time=new Date(System.currentTimeMillis());
		long bet_time=0;
		Date before_time=new Date();
		try {
			before_time=formatter.parse(passtime);
			bet_time=current_time.getTime()-before_time.getTime();
			Log.v(formatter.format(before_time),formatter.format(current_time));
			Log.v(bet_time+"","tag");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(bet_time<60*1000){
			return "刚刚";
		}else if(bet_time>=60*1000&&bet_time<60*60*1000){
			return (bet_time/(60*1000)+" 分钟前");
		}else if(bet_time>=60*60*1000&&bet_time<24*60*60*1000){
			return (bet_time/(60*60*1000)+" 小时前");
		}else if(bet_time>=24*60*60*1000&&(bet_time/365)<(24*60*60*1000)){
			return (bet_time/(24*60*60*1000)+" 天前");
		}else if((bet_time/365)>(24*60*60*1000)){
			return (format.format(before_time).toString());
		}
		return "error";
	}
	public class BitmapCache implements ImageLoader.ImageCache {

		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}

}
