package com.android.hyc.hyc_final;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import Face.BitmapUtils;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ImageView imageview=(ImageView)findViewById(R.id.backdrop);

        ViewTreeObserver vto = imageview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.bg, imageview.getWidth(), imageview.getHeight());
                imageview.setImageBitmap(bitmap);
            }
        });





    }
}
