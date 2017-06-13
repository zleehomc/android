package com.android.hyc.hyc_final;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Face.Adpater.MomentRecyclerViewAdpater;
import Face.JavaBean.Momentinfo;
import Face.MyJsonArrayRequest;

/**
 * Created by hyc on 2016/3/23.
 */
public class OneFragment extends android.support.v4.app.Fragment {
	private RecyclerView recyclerView;
	private LinearLayoutManager layoutManager;
	private MomentRecyclerViewAdpater madpater;
	private SwipeRefreshLayout swipeRefreshLayout;
	private List<Momentinfo> momentinfoList;
	private RequestQueue mQueue;
	private FloatingActionMenu floatingActionMenu;
	private int mPreviousVisibleItem;
	private boolean isScrollToTop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_one,container,false);
		mQueue= Volley.newRequestQueue(getContext());
		MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest(getResources().getString(R.string.ipwireless)+"/appp/index.php/Sql_Do/index/select%20moment.Moment_Id,moment.Moment_content,moment.User_Id,t_user.UserName,moment.Moment_send_time%20from%20moment,t_user%20where%20moment.User_Id=t_user.UserId",
				new MyNetListener(),new MyErrorListener());
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_index);
		mQueue.add(myJsonArrayRequest);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(getContext());
		layoutManager.setOrientation(OrientationHelper.VERTICAL);
		floatingActionMenu=(FloatingActionMenu) view.findViewById(R.id.menu_green);
		recyclerView.setLayoutManager(layoutManager);
		momentinfoList =new ArrayList<>();
		madpater = new MomentRecyclerViewAdpater(momentinfoList, getContext(), isScrollToTop);
		recyclerView.setAdapter(madpater);
		swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget_index);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
		swipeRefreshLayout.setOnRefreshListener(new MyRefresh());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		recyclerView.addOnScrollListener(new MyOnScrollListener());

    }

    class MyOnClickListener implements View.OnClickListener{
        public void onClick(View v){
            switch (v.getId()) {
                default:
                    break;
            }
        }
    }
	class MyRefresh implements SwipeRefreshLayout.OnRefreshListener{

		@Override
		public void onRefresh() {
			MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest(getResources().getString(R.string.ipwireless)+"/appp/index.php/Sql_Do/index/select%20moment.Moment_Id,moment.Moment_content,moment.User_Id,t_user.UserName,moment.Moment_send_time%20from%20moment,t_user%20where%20moment.User_Id=t_user.UserId",
					new MyNetListener(),new MyErrorListener());

			mQueue.add(myJsonArrayRequest);


		}
	}
	class MyOnScrollListener extends RecyclerView.OnScrollListener {
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			if(Math.abs(dy)>4)
			{
				if (dy > 0) {
					floatingActionMenu.hideMenu(true);
				} else {
					floatingActionMenu.showMenu(true);
				}
			}
			if (dy > 0) {
				isScrollToTop = false;
				//floatingActionMenu.hideMenu(false);
			} else {
				isScrollToTop = true;
				//floatingActionMenu.hideMenu(false);
			}
		}
	}
	//volley返回数据
	class MyNetListener implements Response.Listener<JSONArray>{
		@Override
		public void onResponse(JSONArray jsonarray) {
			JSONObject jsonObject;
			Gson gson=new Gson();
			momentinfoList.clear();
			for(int i=0;i<jsonarray.length();i++) {
				try {
					jsonObject=jsonarray.getJSONObject(i);
					momentinfoList.add(gson.fromJson(jsonObject.toString(),Momentinfo.class));
					//ImageRequest imageRequest = new ImageRequest(getResources().getString(R.string.ipwireless)+"/appp/pic/"+jsonObject.getString("User_Id")+".jpg",new MyBitMapNetListerner(),60,60,Bitmap.Config.ARGB_8888,new MyErrorListener());
					//mQueue.getCache().clear();
					//mQueue.add(imageRequest);.88
					//Log.v("Tag",jsonObject.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			swipeRefreshLayout.setRefreshing(false);
			madpater.notifyDataSetChanged();

		}
	}
	//Volley 错误提示
	class MyErrorListener implements Response.ErrorListener{
		@Override
		public void onErrorResponse(VolleyError volleyError) {
			Log.e("TAG", volleyError.getMessage(), volleyError);
		}
	}



}
