package Face;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by hyc on 2016/5/2.
 */
public class MyErrorListener implements Response.ErrorListener{
	@Override
	public void onErrorResponse(VolleyError volleyError) {
		Log.e("TAG", volleyError.getMessage(), volleyError);
	}
}
