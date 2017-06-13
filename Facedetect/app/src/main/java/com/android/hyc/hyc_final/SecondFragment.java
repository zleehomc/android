package com.android.hyc.hyc_final;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
<<<<<<< HEAD
import com.android.volley.VolleyError;
=======
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.PieData;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Face.Adpater.UserRecyclerViewAdapter;
import Face.BitmapUtils;
import Face.FaceUtil;
import Face.JavaBean.User;
import Face.JavaBean.Userinfo;
import Face.MyErrorListener;
import Face.MyJsonArrayRequest;
import Face.Tools;
import Face.sortDate;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyc on 2016/3/26.
 */
public class SecondFragment extends Fragment {

    //监考人员变量
    private static final String LOG_TAG = "SecondFragment_tag";
    private final int REQUEST_CAMERA_IMAGE = 2;
    public final static int REQUEST_CROP_IMAGE = 3;
    private RequestQueue mQueue;
    private Toast mToast;
    static Boolean load_flag = false;
    private RecyclerView recyclerView;
    private List<Userinfo> usersList;
    private byte[] mImageData = null;
    private FaceRequest mFaceRequest;
    private UserRecyclerViewAdapter adapter;
    private boolean isScrollToTop;
    private LinearLayoutManager layoutManager;
    private TextView textView_title;
    private TextView textview_title_enter_time;
    private TextView textView_title_exit_time;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View view;

    //学生卡界面变量
<<<<<<< HEAD
    public TextView CardSiteTv, CardPlaceTv, CardUsernameTv, CardNumbTv,cardExamNameTv;
    private ImageView imageView_card_photo;
    public MaterialSpinner Spinner_exam_card;
    public List<String> exam_card_select_name = new ArrayList<>();
=======
    public TextView examCardSiteTv, examCardPlaceTv, examCardUsernameTv, examCardNumbTv;
    private ImageView imageView_card_photo;
    public MaterialSpinner Spinner_exam_card;
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5


    public Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.notifyDataSetChanged();

                        break;
                    default:
                        break;
                }
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (User.get_user_power() == 2) {
            view = inflater.inflate(R.layout.fragment_second, container, false);
            User.secondFragmentTag = getTag();
            textView_title = (TextView) view.findViewById(R.id.title_textview);
            textview_title_enter_time = (TextView) view.findViewById(R.id.title_textview_enter_time);
            textView_title_exit_time = (TextView) view.findViewById(R.id.title_textview_exit_time);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            usersList = new ArrayList<>();
            adapter = new UserRecyclerViewAdapter(usersList, getContext(), isScrollToTop);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnScrollListener(new MyOnScrollListener());
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            swipeRefreshLayout.setOnRefreshListener(new MyRefresh());
        } else if (User.get_user_power() == 1) {
            view = inflater.inflate(R.layout.activity_exam_card, container, false);
            //<editor-fold desc="init view">
<<<<<<< HEAD
            CardNumbTv = (TextView) view.findViewById(R.id.exam_card_numb_tv);
            CardPlaceTv = (TextView) view.findViewById(R.id.exam_card_place_tv);
            CardSiteTv = (TextView) view.findViewById(R.id.exam_card_site_tv);
            CardUsernameTv = (TextView) view.findViewById(R.id.exam_card_username_tv);
            imageView_card_photo = (ImageView) view.findViewById(R.id.exam_card_photo_iv);
            Spinner_exam_card=(MaterialSpinner)view.findViewById(R.id.spinner_exam_card);
            cardExamNameTv=(TextView)view.findViewById(R.id.exam_card_exam_name_tv);
=======
            examCardNumbTv = (TextView) view.findViewById(R.id.exam_card_numb_tv);
            examCardPlaceTv = (TextView) view.findViewById(R.id.exam_card_place_tv);
            examCardSiteTv = (TextView) view.findViewById(R.id.exam_card_site_tv);
            examCardUsernameTv = (TextView) view.findViewById(R.id.exam_card_username_tv);
            imageView_card_photo = (ImageView) view.findViewById(R.id.exam_card_photo_iv);
            Spinner_exam_card=(MaterialSpinner)view.findViewById(R.id.spinner_exam_card);
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
            //</editor-fold>
            mQueue = Volley.newRequestQueue(getContext());

            //加载考试卡页面的图片
<<<<<<< HEAD
            ImageRequest imageRequest = new ImageRequest("http://facedetect.bj.bcebos.com/pic/" + User.get_userid() + ".jpg",
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imageView_card_photo.setImageBitmap(bitmap);
                        }
                    }, 200, 200, Bitmap.Config.ALPHA_8, new MyErrorListener());
            mQueue.add(imageRequest);
            //加载下拉选项的不同考试信息
            Log.v("tag","load_spinner_exam_card");
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(getString(R.string.ipwireless) + "/appp/index.php/Sql_Do/index/select * from t_ass where t_ass.studentid =" + User.get_userid(),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    exam_card_select_name.add(jsonArray.getJSONObject(i).getString("ass_id"));
                                    Log.v("spinner:ass_id:", jsonArray.getJSONObject(i).getString("ass_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Spinner_exam_card.setItems(exam_card_select_name);
                        }
                    },
                    new MyErrorListener());
            mQueue.add(jsonArrayRequest);
            Spinner_exam_card.animate();
            exam_card_select_name.clear();
            exam_card_select_name.add("请选择考证");
            Spinner_exam_card.setItems(exam_card_select_name);
=======
            ImageRequest imageRequest = new ImageRequest("http://facedetect.bj.bcebos.com/pic/" + User.get_userid() + ".jpg", new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    imageView_card_photo.setImageBitmap(bitmap);
                }
            }, 200, 200, Bitmap.Config.ALPHA_8, new MyErrorListener());
            mQueue.add(imageRequest);
            //加载下拉选项的不同考试信息
            Log.v("tag","load_spinner_exam_card");
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(getString(R.string.ipwireless)+"/appp/index.php/Sql_Do/index/select * from t_ass where t_ass.studentid ="+ User.get_userid(), new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    jsonArray.length();
                    List<String> exam_card_select_name = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        try {
                            exam_card_select_name.add(jsonArray.getJSONObject(i).getString("ass_id"));
                            Log.v("spinner:ass_id:",jsonArray.getJSONObject(i).getString("ass_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Spinner_exam_card.setItems(exam_card_select_name);
                }
            },new MyErrorListener());
            mQueue.add(jsonArrayRequest);
            Spinner_exam_card.animate();
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
            Spinner_exam_card.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    //第二个参数为http的方法，null默认方法为get方法
                    JsonArrayRequest jsonArray_Request_info_exam_card=new JsonArrayRequest(
<<<<<<< HEAD
                            getString(R.string.ipwireless)+"/appp/index.php/Sql_Do/index/select * from t_ass,exam,site where t_ass.ass_id="+item+"&&t_ass.examid=exam.ExamId&&t_ass.siteid=site.SiteId",
=======
                            getString(R.string.ipwireless)+"/appp/index.php/Sql_Do/index/select * from t_ass where t_ass.ass_id="+item,
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {
                                    try {
<<<<<<< HEAD
                                        CardSiteTv.setText("座  位  号:"+jsonArray.getJSONObject(0).getString("seatid"));
                                        CardPlaceTv.setText("考点名称:"+jsonArray.getJSONObject(0).getString("SiteName"));
                                        CardUsernameTv.setText("考生姓名:"+User.get_username());
                                        CardNumbTv.setText("考       号:"+jsonArray.getJSONObject(0).getString("ass_id"));
                                        cardExamNameTv.setText("考试名称:"+jsonArray.getJSONObject(0).getString("ExamName"));
=======
                                        examCardSiteTv.setText(jsonArray.getJSONObject(0).getString("seatid"));
                                        examCardPlaceTv.setText(jsonArray.getJSONObject(0).getString("examid"));
                                        examCardUsernameTv.setText(User.get_username());
                                        examCardNumbTv.setText(jsonArray.getJSONObject(0).getString("ass_id"));
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },new MyErrorListener());
                    mQueue.add(jsonArray_Request_info_exam_card);

                }
            });

        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                isScrollToTop = false;
            } else {
                isScrollToTop = true;
            }
        }
    }

    class MyRefresh implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (User.get_login_status()) {
                        load_flag = true;
                        load_page();
                        Log.v("fdf", "fdaf");
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast toast;
                        toast = Toast.makeText(getActivity(),
                                "请登录后查看考场信息", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }).start();
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v("setUserVisibleHint", "setUserVisibleHint");
        if (isVisibleToUser && User.get_login_status() && !load_flag && User.get_user_power() == 2) {
            load_flag = true;
            Log.v("isVisibleToUser", "isVisibleToUser");
            //recyclerView.setAdapter(adapter);
            Thread thread = new Thread() {
                public void run() {
                    load_page();
                }
            };
            thread.start();
        } else if (isVisibleToUser && !User.get_login_status()) {

            Toast toast;
            toast = Toast.makeText(getActivity(),
                    "请登录后查看考场信息", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void onResume() {
        super.onResume();
        Log.v("onResume_second", "onResume");


    }

    public void onPause() {
        super.onPause();
        Log.v("onPause", "onPause");
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
                Date current_time = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                StringRequest stringRequest_modify_status = new StringRequest(getResources().getString(R.string.ipwireless) + "/appp/index.php/Sql_Do/update_ass_status/" + User.temp2 + "/1",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("TAG", response);
                            }
                        }, new MyErrorListener());

                StringRequest stringRequest_modify_enter_time = new StringRequest(getResources().getString(R.string.ipwireless) + "/appp/index.php/Sql_Do/update_ass_enter_time/" + User.temp2 + "/" + formatter.format(current_time), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("Tag", s);
                    }
                }, new MyErrorListener());

                Log.v(formatter.format(current_time), current_time.toString());
                mQueue.add(stringRequest_modify_status);
                mQueue.add(stringRequest_modify_enter_time);
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
                } else if ("verify".equals(type)) {
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
            }
        }

        @Override
        public void onCompleted(SpeechError error) {


            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(getContext(), new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("----->", "dft");
       /* if (resultCode != RESULT_OK) {
            Log.v("------>", "5");
            return;
        }*/
        Log.v("------>", "6");
        String fileSrc = null;
        if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (data != null) {
                Log.v(LOG_TAG, "data is not null!");
                Toast.makeText(getContext(), "Image saved to:\n" + data.getData(),
                        Toast.LENGTH_LONG).show();
                Log.v(LOG_TAG, data.getData() + "");
                if (data.hasExtra("data")) {
                    mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                    mFaceRequest = new FaceRequest(getContext());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap thumbnail = data.getParcelableExtra("data");

                    //可根据流量及网络状况对图片进行压缩
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    mImageData = baos.toByteArray();
                    Log.v("mAuthid", User.temp);
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, User.temp);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                }

            } else {
                Log.v(LOG_TAG, "data is null!");
                fileSrc = User.mPictureFile.getAbsolutePath();
                // 跳转到图片裁剪页面
                Log.v("------>", "14");
                updateGallery(fileSrc);
                FaceUtil.cropPicture(getActivity(), Uri.fromFile(new File(fileSrc)));
            }
            //FaceUtil.cropPicture(getActivity(), Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == REQUEST_CROP_IMAGE) {
            fileSrc = FaceUtil.getImagePath(getContext());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);


            // 若mImageBitmap为空则图片信息不能正常获取
            if (null == mImage) {
                showTip("图片信息无法正常获取！");
                Log.v("------>", "图片信息无法正常获取");
                return;
            }
            Log.v("------>", "图片信息正常获取");
            Log.v("------>", mImage.toString());
            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }
            mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
            mFaceRequest = new FaceRequest(getContext());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            mImageData = baos.toByteArray();

            //可根据流量及网络状况对图片进行压缩
            Log.v("mAuthid", User.temp);
            mFaceRequest.setParameter(SpeechConstant.AUTH_ID, User.temp);
            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
            mFaceRequest.sendRequest(mImageData, mRequestListener);

<<<<<<< HEAD
        }
=======
        }

    }

    class MyNetListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray jsonarray) {
            try {
                JSONObject jsonObject = jsonarray.getJSONObject(0);
                textView_title.setText("考试名称:" + jsonObject.getString("ExamName"));
                textview_title_enter_time.setText("入场时间:" + jsonObject.getString("Enter_Time"));
                textView_title_exit_time.setText("截止时间:" + jsonObject.getString("Exit_Time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5

    private void load_page() {
        Log.v("tag", "load_page");
        mQueue = Volley.newRequestQueue(getContext());
        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest(getResources().getString(R.string.ipwireless) + "/appp/index.php/Sql_Do/index/select%20exam.ExamId,exam.ExamName,exam.Enter_Time,exam.Exit_Time%20from%20exam%20where%20exam.Teacher_Id=" + User.get_userid(),
                new MyNetListener(), new MyErrorListener());
        mQueue.add(myJsonArrayRequest);
        //Thread thread = new Thread() {
        //public void run() {
        Tools tools = new Tools(getString(R.string.ipwireless));
        String sql = "select t_ass.ass_id,exam.ExamName,site.SiteName,t_user.UserId,t_user.UserName,t_ass.seatid,t_ass.ass_status from t_user,t_ass,exam,site where exam.Teacher_Id=" + User.get_userid() + "&&t_user.t_Status=1&&t_user.UserId=t_ass.studentid&&site.SiteId=t_ass.siteid&&t_ass.examid=exam.ExamId";
        JSONArray jsonArray = tools.load_data(sql);
        String path;
        File mFile;
        Bitmap bitmap;
        usersList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                path = "/sdcard/SmartCloud/myImage/" + jsonObject.getString("UserId") + ".jpg";
                mFile = new File(path);
                if (mFile.exists()) {
                    bitmap = BitmapUtils.decodeSampledBitmapFromFile(path, 1000, 1000);
                    usersList.add(new Userinfo(jsonObject.getString("UserId"), jsonObject.getString("UserName"), jsonObject.getString("ass_id"), bitmap, jsonObject.getString("seatid"), jsonObject.getString("ass_status")));
                    Log.v(jsonObject.getString("ass_id"), "已经加入adapter");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message();
        message.what = 1;
        mhandler.sendMessage(message);
    }
<<<<<<< HEAD

    class MyNetListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray jsonarray) {
            try {
                JSONObject jsonObject = jsonarray.getJSONObject(0);
                textView_title.setText("考试名称:" + jsonObject.getString("ExamName"));
                textview_title_enter_time.setText("入场时间:" + jsonObject.getString("Enter_Time"));
                textView_title_exit_time.setText("截止时间:" + jsonObject.getString("Exit_Time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void load_page() {
        Log.v("tag", "load_page");
        mQueue = Volley.newRequestQueue(getContext());
        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest(getResources().getString(R.string.ipwireless) + "/appp/index.php/Sql_Do/index/select%20exam.ExamId,exam.ExamName,exam.Enter_Time,exam.Exit_Time%20from%20exam%20where%20exam.Teacher_Id=" + User.get_userid(),
                new MyNetListener(), new MyErrorListener());
        mQueue.add(myJsonArrayRequest);
        //Thread thread = new Thread() {
        //public void run() {
        Tools tools = new Tools(getString(R.string.ipwireless));
        String sql = "select t_ass.ass_id,exam.ExamName,site.SiteName,t_user.UserId,t_user.UserName,t_ass.seatid,t_ass.ass_status from t_user,t_ass,exam,site where exam.Teacher_Id=" + User.get_userid() + "&&t_user.t_Status=1&&t_user.UserId=t_ass.studentid&&site.SiteId=t_ass.siteid&&t_ass.examid=exam.ExamId";
        JSONArray jsonArray = tools.load_data(sql);
        String path;
        File mFile;
        Bitmap bitmap;
        usersList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                path = "/sdcard/SmartCloud/myImage/" + jsonObject.getString("UserId") + ".jpg";
                mFile = new File(path);
                if (mFile.exists()) {
                    bitmap = BitmapUtils.decodeSampledBitmapFromFile(path, 1000, 1000);
                    usersList.add(new Userinfo(jsonObject.getString("UserId"), jsonObject.getString("UserName"), jsonObject.getString("ass_id"), bitmap, jsonObject.getString("seatid"), jsonObject.getString("ass_status")));
                    Log.v(jsonObject.getString("ass_id"), "已经加入adapter");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message();
        message.what = 1;
        mhandler.sendMessage(message);
    }
=======
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5

}



