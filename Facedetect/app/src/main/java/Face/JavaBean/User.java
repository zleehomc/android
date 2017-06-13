package Face.JavaBean;

import java.io.File;

/**
 * Created by hyc on 2016/3/24.
 * 用户信息
 */
public class User  {
	public static String secondFragmentTag;
    public static String temp;
	public static String temp2;
    public static  File mPictureFile;
    private static String username=null;
    private static String userid=null;
	private static boolean login_status=false;
	private static int  user_power=-1;
	public static int get_user_power()
	{
		return user_power;
	}
	public static void set_user_power(int user_power)
	{
	    User.user_power=user_power;
	}
    public static String get_username()
    {
        return username;
    }
    public static void  set_username(String username)
    {
        User.username=username;
    }
    public static String get_userid()
    {
        return userid;
    }
    public static void  set_userid(String userid)
    {
        User.userid=userid;
    }
    public static Boolean get_login_status()
    {
        return login_status;
    }
    public static void  set_login_status(Boolean login_status)
    {
        User.login_status=login_status;
    }






}
