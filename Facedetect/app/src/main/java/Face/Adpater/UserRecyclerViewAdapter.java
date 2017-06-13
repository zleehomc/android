package Face.Adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hyc.hyc_final.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import Face.CircleImageView;
import Face.JavaBean.User;
import Face.JavaBean.Userinfo;

/**
 * Created by QiWangming on 2015/6/13.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>{
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private final int REQUEST_CAMERA_IMAGE = 2;
    private List<Userinfo> useres;
    private Context context;
    private boolean isScrollToTop;
    private FaceRequest mFaceRequest;
    private byte[] mImageData = null;
    private Toast mToast;
    private File mPictureFile;
    public UserRecyclerViewAdapter(List<Userinfo> useres,Context context,boolean isScrollToTop) {
        this.useres = useres;
        this.context=context;
        this.isScrollToTop=isScrollToTop;
    }


    //自定义ViewHolder类
    static class UserViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CircleImageView user_photo;
        TextView exam_id;
        TextView user_name;
        TextView seat_numb;
        ButtonRectangle share;
        ButtonRectangle readMore;

        public UserViewHolder(final View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.card_view);
            user_photo= (CircleImageView) itemView.findViewById(R.id.user_civ);
            exam_id= (TextView) itemView.findViewById(R.id.exam_it_tv);
            user_name= (TextView) itemView.findViewById(R.id.user_name_tv);
            seat_numb=(TextView)itemView.findViewById(R.id.seat_numb_tv);
            share= (ButtonRectangle) itemView.findViewById(R.id.btn_share);
            readMore= (ButtonRectangle) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            //news_title.setBackgroundColor(Color.argb(20, 0, 0, 0));

        }
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        UserViewHolder nvh=new UserViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder personViewHolder, int i) {
        final int j=i;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        personViewHolder.user_photo.setImageBitmap(useres.get(i).getPhoto());
        personViewHolder.exam_id.setText("考号:"+useres.get(i).getAssid());
        personViewHolder.user_name.setText("姓名:" + useres.get(i).getUsername());
        personViewHolder.seat_numb.setText("座位号:"+useres.get(i).getSeatnumb());
        if(useres.get(i).getAss_status().equals("1")) {
            personViewHolder.share.setClickable(false);
           // personViewHolder.share.setText("该考生已经入考场");
        }
        if (isScrollToTop) {//根据滑动方向设置动画
            personViewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_in_top_anim));
        } else {
            personViewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_in_bottom_anim));
        }
        personViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread=new Thread(){
                    public void run(){
                        SpeechUtility.createUtility(context, "appid=" + "56d80d7a");
                        mFaceRequest = new FaceRequest(context);
                        Bitmap bitmap=useres.get(j).getPhoto();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        mImageData = baos.toByteArray();
                        //可根据流量及网络状况对图片进行压缩
                        Log.v("---->", useres.get(j).getUserid());
                        mFaceRequest.setParameter(SpeechConstant.AUTH_ID, "hyctest733384f_"+useres.get(j).getUserid());
                        mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                        mFaceRequest.sendRequest(mImageData, mRequestListener);
                        mPictureFile = new File(Environment.getExternalStorageDirectory(),
                                "picture" + System.currentTimeMillis()/1000 + ".jpg");
                        Intent mIntent = new Intent();
                        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        User.mPictureFile=mPictureFile;
                        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                        mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        User.temp="hyctest733384f_"+useres.get(j).getUserid();
						User.temp2=useres.get(j).getAssid();
                        Log.v("----->", "xxxx");
                        ((Activity)context).startActivityForResult(mIntent,REQUEST_CAMERA_IMAGE);
                    }
                };
                thread.start();

            }
        });
/*
        personViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SecondFragment.class);
                intent.putExtra("News",newses.get(j));
                context.startActivity(intent);
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return useres.size();
    }
    @Override   //视图不可见时回收动画
    public void onViewDetachedFromWindow(UserViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }




    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            showTip("注册成功");
        } else {
            showTip("注册失败");
        }
    }

    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("验证失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                showTip("通过验证，欢迎回来！");
            } else {
                showTip("验证不通过");
            }
        } else {
            showTip("验证失败");
        }
    }







    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                }
                else if ("verify".equals(type)) {
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError error) {

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        Log.v("","authid已经被注册，请更换后再试");
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };











    private void showTip(final String str) {
        Log.v(str,str);
        mToast.setText(str);

        mToast.show();
    }
}
