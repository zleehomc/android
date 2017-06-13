package cn.harrysean.veisky.data;

import org.json.JSONArray;

public abstract class VskCallback {
	public void onSuccess(JSONArray response){}
	public void onError(String errCode,String errMessage){}
}
