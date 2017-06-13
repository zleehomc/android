package com.android.hyc.hyc_final;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import Face.Adpater.MenuRecyclerViewAdapter;
import Face.BitmapUtils;
import Face.JavaBean.Menuinfo;
import Face.JavaBean.User;
import Face.JavaBean.myUserDetail;
import Face.MyErrorListener;
import Face.Tools;

public class ThirdFragment extends Fragment {
    private String Path_download;
    private String Path_judge;
    private InputStream inputStream;
    private  ProgressBar progressBar;
    private URLConnection connection;
    private int FileLength;
    private int DownedFileLength = 0;
	private TextView textView;
    private RequestQueue requestQueue;

	public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_third, container, false);
        if(isAdded())
        {
            Path_download=getResources().getString(R.string.ipwireless)+"/appp/index.php/Search_Do/pic_download/";
            Path_judge = getResources().getString(R.string.ipwireless)+"/appp/index.php/Search_Do/pic_judge/";

        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.m_recyclerView);
		Log.v("ThirdFragment","oncreateView");

		List<Menuinfo> menuList = new ArrayList<>();
        menuList.add(new Menuinfo("我的信息",R.drawable.ic_person_black_48dp));
        menuList.add(new Menuinfo("考场统计",R.drawable.ic_textsms_black_48dp));
        menuList.add(new Menuinfo("考生信息离线下载", R.drawable.ic_cloud_download_black_48dp));
        if(User.get_user_power()==1){
            menuList.clear();
            menuList.add(new Menuinfo("我的信息",R.drawable.ic_person_black_48dp));
    }

		MenuRecyclerViewAdapter adapter = new MenuRecyclerViewAdapter(menuList, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).showLastDivider().build());
        adapter.setOnItemClickListener(new MenuRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View parent, int position) {
                switch (position) {
                    case 0:
                        Toast.makeText(getContext(),"我的信息", Toast.LENGTH_SHORT).show();
                        requestQueue= Volley.newRequestQueue(getContext());
                        final myUserDetail myUserDetail = new myUserDetail();
                        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(getString(R.string.ipwireless)+
                                "/appp/index.php/Sql_Do/index/select * from t_user where UserId="+ User.get_userid(),
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray jsonArray) {

                                        //// TODO: 2016/5/30
                                        try {
                                            Log.v("tag",jsonArray.toString());
                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            myUserDetail.setUserId(jsonObject.getString("UserId"));
                                            myUserDetail.setUserName(jsonObject.getString("UserName"));
                                            myUserDetail.setEmail(jsonObject.getString("email"));
                                            myUserDetail.setT_Status(jsonObject.getString("t_Status"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Intent intent=new Intent(getContext(),userInfoActivity.class);
                                        Log.v("11",myUserDetail.getEmail());
                                        intent.putExtra("info",myUserDetail);
                                        startActivity(intent);
                                    }
                                },new MyErrorListener());
                        requestQueue.add(jsonArrayRequest);
                        break;
                    case 1:
                        Toast.makeText(getContext(),"考场统计", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), Result_history.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(getContext(),"考生信息离线下载", Toast.LENGTH_SHORT).show();
                        final View offline_download = inflater.inflate(R.layout.offline_download, null);
                        textView=(TextView)offline_download.findViewById(R.id.progress_tv);
                        progressBar=(ProgressBar)offline_download.findViewById(R.id.progressBarIndeterminate);
                        final TextView network_status=(TextView)offline_download.findViewById(R.id.network_status_tv);
                        final ButtonRectangle getButton=(ButtonRectangle)offline_download.findViewById(R.id.get_button);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        final ImageView imageview=(ImageView)offline_download.findViewById(R.id.imageview_header);
                        Tools tools =new Tools(getString(R.string.ipwireless));
                        network_status.setText("网络状态:" + tools.getNetworkType(getActivity()));
                        getButton.setOnClickListener(new MyOnclick());
                        builder.setView(offline_download);
                        builder.show();
                        ViewTreeObserver vto = imageview.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                imageview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                Bitmap bitmap= BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.download, imageview.getWidth(), imageview.getHeight());
                                imageview.setImageBitmap(bitmap);
                            }
                        });
                        break;
                    default:
                        break;

                }
            }
        });
        return view;

    }



    public class MyOnclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.get_button:
                    Thread thread = new Thread() {
                        public void run() {
                            Tools tools = new Tools(getString(R.string.ipwireless));
                            String sql="select t_ass.ass_id,exam.ExamName,site.SiteName,t_user.UserId,t_user.UserName,t_ass.seatid from t_user,t_ass,exam,site where exam.Teacher_Id="+User.get_userid()+"&&t_user.t_Status=1&&t_user.UserId=t_ass.studentid&&site.SiteId=t_ass.siteid&&t_ass.examid=exam.ExamId";
                            JSONArray jsonArray = tools.load_data(sql);
                            for (int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    DownFile(jsonObject.getString("UserId"));
                                    Message message2 = new Message();
                                    message2.what = 3;
                                    message2.arg1=i+1;
                                    message2.arg2=jsonArray.length()+1;
                                    handler.sendMessage(message2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Message message3 = new Message();
                            message3.what = 2;
                            handler.sendMessage(message3);

                        }
                    };
                    thread.start();
                    break;
                default:
                    break;
            }
        }
    }
    private String judgeFile(String Filname) {
        try {
            Log.v("0", "1Fff");
            HttpClient httpclient = new DefaultHttpClient();
            String url = Path_judge + Filname;
            HttpPost httppost = new HttpPost(url);
            Log.v("1", "1Fff");
            //添加http头信息
            httppost.addHeader("Authorization", "your token"); //认证token
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("User-Agent", "imgfornote");
            Log.v("2", "1Fff");
            //http post的json数据格式：  {"name": "your name","parentId": "id_of_parent"}
            JSONObject obj = new JSONObject();
            httppost.setEntity(new StringEntity(obj.toString()));
            Log.v("3", "1Fff");
            HttpResponse response;
            response = httpclient.execute(httppost);
            //检验状态码，如果成功接收数据
            Log.v("4", "1Fff");
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                Log.v("5", "1Fff");
                String rev = EntityUtils.toString(response.getEntity());//返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
                obj = new JSONObject(rev);
                String id = obj.getString("status");
                Log.v(id, "Fff");
                return id;
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }

        Log.v("sss", "Fff");
        return "sss";
    }
    private void DownFile(String Filname) {
        if (judgeFile(Filname).equals("0")) {
            Log.v("sss", "Fff");
            Message messagett = new Message();
            messagett.what = 6;
            handler.sendMessage(messagett);

        } else {
            DownedFileLength=0;
            String urlString = Path_download + Filname;
            try {
                URL url = new URL(urlString);
                connection = url.openConnection();
                if (connection.getReadTimeout() == 5) {
                    Log.i("---------->", "当前网络有问题");
                }
                inputStream = connection.getInputStream();
                Log.v(inputStream.toString(), "1");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String save_App_Path = "/sdcard/SmartCloud";
            String save_Path="/sdcard/SmartCloud/myImage";
            File file1 = new File(save_App_Path);
            File file2=new File(save_Path);
            if (!file1.exists()) {
                file1.mkdir();

            }
            if(!file2.exists()){
                file2.mkdir();
            }
            String savePathString = "/sdcard/SmartCloud/myImage/" + Filname + ".jpg";
            File file = new File(savePathString);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Message message = new Message();
            try {
				OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 4];
                FileLength = connection.getContentLength();
                int len = -1;
                message.what = 0;
                handler.sendMessage(message);
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                    DownedFileLength += len;
                    Message message1 = new Message();
                    message1.what = 1;
                    handler.sendMessage(message1);
                }
                /*Message message2 = new Message();
                message2.what = 2;
                handler.sendMessage(message2);*/
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private Handler handler;{
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
						case 0:
							progressBar.setMax(FileLength);
							Log.i("文件长度----------->", FileLength + "");
							break;
						case 1:
							progressBar.setSecondaryProgress(DownedFileLength);
							int x = DownedFileLength * 100 / FileLength;
							textView.setText("下载进度：" + x + "%");
							//Log.v("DownedFileLength"+DownedFileLength+"FileLength"+FileLength,"x"+x);
							break;
						case 2:
							Toast.makeText(getActivity().getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
							progressBar.setProgress(0);
							textView.setText("下载完成");
							break;
						case 3:
							Log.v("xzzxxxxxxxx", msg.arg1 + "");
							progressBar.setProgress(FileLength / msg.arg2 * msg.arg1);
							break;
						case 6:
							Toast.makeText(getActivity().getApplicationContext(), "文件错误", Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
					}
				}
			}
		};
	}
}
