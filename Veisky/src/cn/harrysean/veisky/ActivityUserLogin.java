package cn.harrysean.veisky;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.harrysean.veisky.data.UserLoginParam;
import cn.harrysean.veisky.data.VskCallback;
import cn.harrysean.veisky.data.VskClient;
import cn.harrysean.veisky.data.Param.pTypeEnum;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUserLogin extends FinalActivity  {
	private SharedPreferences sp;  
	@ViewInject(id = R.id.btnlogin)
	Button btnlogin;
	@ViewInject(id = R.id.tvEmail)
	TextView email;
	@ViewInject(id = R.id.tvPassword)
	TextView pass;
	@ViewInject(id = R.id.chkRemPass)
	CheckBox remPass;
	@ViewInject(id = R.id.chkAutoLogin)
	CheckBox autoLogin;
	
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userlogin);
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		if(sp.getBoolean("ISCHECK", false))
		{
			remPass.setChecked(true);
			email.setText(sp.getString("UserName", ""));
			pass.setText(sp.getString("UserPassword", ""));
			
			
			
			
			
		}
		
		
		
		
		
		btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	
            	
            	
            	
            	loginclick(v);
            }
		});
		
	}
	public void loginclick(View v){
		if(pass.getText().toString().equals("")||email.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "请不要留空",Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		VskClient clt = new VskClient(this);
		UserLoginParam param = new UserLoginParam(pTypeEnum.user_login);
		param.setPassword(pass.getText().toString());
		param.setEmail(email.getText().toString());
		final ProgressDialog dialog= new ProgressDialog(ActivityUserLogin.this);
		dialog.setMessage("登陆中，请稍等 …");
		dialog.setIndeterminate(true);
		dialog.show();
		clt.sendRequest(param, new VskCallback() {
			@Override
			public void onSuccess(JSONArray response) {
				dialog.dismiss();
				try {
					
					JSONObject jd = (JSONObject)response.getJSONObject(0);
					if(jd.getInt("success")==1){
						if(remPass.isChecked()){
						sp.edit().putBoolean("ISCHECK", true).commit(); 
						sp.edit().putString("UserName",email.getText().toString() ).commit(); 
						sp.edit().putString("UserPassword",pass.getText().toString() ).commit(); 
						
						}
						else{
						sp.edit().putBoolean("ISCHECK", false).commit(); 
						}
						Toast.makeText(getApplicationContext(), jd.getString("info"),Toast.LENGTH_SHORT).show();
						((Config)getApplicationContext()).setUserLogin(true,jd.getInt("id"));
						ActivityUserLogin.this.finish();
					}else{
						Toast.makeText(getApplicationContext(), jd.getString("info"),Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String errCode, String errMessage) {
				Toast.makeText(getApplicationContext(), errMessage,Toast.LENGTH_SHORT).show();
			}
		});
	}

}
