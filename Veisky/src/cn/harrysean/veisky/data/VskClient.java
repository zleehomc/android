package cn.harrysean.veisky.data;

import org.json.JSONArray;
import org.json.JSONException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.util.Log;

public class VskClient {
	private VskCallback cb;
	private Context ct;
	public VskClient(Context context){
		ct=context;
	}
	public void sendRequest(final Param param,VskCallback callback){
		cb=callback;
		FinalHttp fh = new FinalHttp();
        fh.post(param.getUrl(),param.getParams(), new AjaxCallBack<String>(){
        	@Override
            public void onLoading(long count, long current) {
            	
            }
        	@Override
			public void onSuccess(String t) {
        		Log.v("resp", t);


        		JSONArray ja;
				try {
					ja = new JSONArray(t);
					cb.onSuccess(ja);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					cb.onError("1", "数据返回错误"+t);
				}
            }
        	@Override
            public void onStart() {
        		
            }
        	@Override
            public void onFailure(Throwable t,int errorNo ,String strMsg) {
                //加载失败的时候回调
        		cb.onError(Integer.valueOf(errorNo).toString(), strMsg);
            }
        });
	}
}
