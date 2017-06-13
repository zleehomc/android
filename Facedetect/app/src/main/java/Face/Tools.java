package Face;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Face.JavaBean.User;

/**
 * Created by hyc on 2016/3/23.
 */
public class Tools {
    private static String ipwireless;
    public Tools(String ipwireless)
    {
        this.ipwireless=ipwireless;
    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public  boolean isUser(String Username,String Password)
    {
        HttpClient httpClient = new DefaultHttpClient();
        String basic_url=ipwireless+"/appp/index.php/Login/pic_judge";
        Log.v(basic_url,"x");
        basic_url+="/"+Username+"/"+Password;
        HttpPost httpPost = new HttpPost(basic_url);
        int res = 0;
        try {
            res = httpClient.execute(httpPost).getStatusLine().getStatusCode();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (res == 200) {
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpPost);
                StringBuilder builder = new StringBuilder();
                BufferedReader bufferedReader2 = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String str2 = "";
                for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
                        .readLine()) {
                    builder.append(s);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                JSONObject jsonobject2=jsonArray.getJSONObject(0);
                int UserId=0;
				int User_power;
                UserId=jsonobject2.getInt("UserId");
                Username=jsonobject2.getString("UserName");
                User_power=jsonobject2.getInt("t_Status");
                Log.v("fff",Username);
                if(UserId!=0)
                {
                    User.set_username(Username);
                    User.set_userid(UserId + "");
                    User.set_login_status(true);
					User.set_user_power(User_power);

                    return true;
                }
                else
                {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public JSONArray load_data(String sql) {
        HttpClient httpClient = new DefaultHttpClient();
        String basic_url = ipwireless+"/appp/index.php/Sql_Do/index/";
        Log.v(basic_url,"x");
        basic_url += sql;
        basic_url=basic_url.replaceAll(" ","%20");
        Log.v("---->",basic_url);
        HttpPost httpPost = new HttpPost(basic_url);
        int res = 0;
        try {
            res = httpClient.execute(httpPost).getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (res == 200) {
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpPost);
                StringBuilder builder = new StringBuilder();
                BufferedReader bufferedReader2 = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String str2 = "";
                for (String s = bufferedReader2.readLine(); s != null; s = bufferedReader2
                        .readLine()) {
                    builder.append(s);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                return jsonArray;
                /*JSONObject jsonobject2=jsonArray.getJSONObject(0);
                int UserId=0;
                String Username=null;
                UserId=jsonobject2.getInt("UserId");
                Username=jsonobject2.getString("UserName");
                    return true;*/
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public String getNetworkType(Activity activity) {
        String netType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return "error";
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if( extraInfo!=null){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = "CMNET";
                } else {
                    netType = "CMWAP";
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "WIFI";
        }
        return netType;
    }

}
