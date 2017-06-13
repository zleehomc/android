package com.android.hyc.hyc_final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


import Face.Adpater.myUserDetailViewAdapter;
import Face.JavaBean.myUserDetail;

public class userInfoActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent intent=this.getIntent();
        final myUserDetail myUserDetail =(myUserDetail)intent.getSerializableExtra("info");
        Log.v("aa",myUserDetail.getEmail());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.item_list_user_info);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        myUserDetailViewAdapter adapter = new myUserDetailViewAdapter(myUserDetail,this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).showLastDivider().build());



    }
}
