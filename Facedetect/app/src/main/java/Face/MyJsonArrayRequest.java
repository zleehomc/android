package Face;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;

/**
 * Created by hyc on 2016/5/2.
 */
public class MyJsonArrayRequest extends JsonArrayRequest{
	public MyJsonArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
		super(url, listener, errorListener);
	}
	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			JSONArray jsonArray = new  JSONArray(new String(response.data, "UTF-8"));
			return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (Exception je) {
			return Response.error(new ParseError(je));
		}
	}
}
