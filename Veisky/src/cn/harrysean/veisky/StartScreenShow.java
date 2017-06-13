package cn.harrysean.veisky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class StartScreenShow extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.startscreen);
		setContentView(new ViewStartScreen(this));
		new Handler().postDelayed(new Runnable(){  
		    public void run() {  
		    	Intent intent = new Intent();
	            intent.setClass(StartScreenShow.this, VskActivity.class);
	            startActivity(intent);
	            //如果不关闭当前的会出现好多个页面
	            StartScreenShow.this.finish();
		    }  
		}, 3000); 
	}
}
