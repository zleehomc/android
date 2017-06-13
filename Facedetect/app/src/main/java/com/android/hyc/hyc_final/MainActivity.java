package com.android.hyc.hyc_final;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;

import Face.BitmapUtils;
import Face.CircleImageView;
import Face.MyErrorListener;
import Face.Tools;
import Face.JavaBean.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
	private final int REQUEST_CAMERA_IMAGE = 2;
	public final static int REQUEST_CROP_IMAGE = 3;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private Tools tools;
    private FragmentManager fragmentManager;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TextView username_text;
    private CircleImageView user_view;
    private Button exit_btn;
    private Button login_btn;
    private ImageView user_iv;
    private ImageView about_iv;
    private ImageView navheader_bgp_iv;
    private View headerView;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        mTablayout= (TabLayout) findViewById(R.id.tabLayout);
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        about_iv=(ImageView)findViewById(R.id.backdrop);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            private String[] mTitles = new String[]{"首页", "考试"};
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new OneFragment();
                } else if (position == 1) {
                    return new SecondFragment();
                } else if (position == 2) {
                    return  new ThirdFragment();
                }
                return new OneFragment();
            }
            @Override
            public int getCount() {
                return mTitles.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        mTablayout.setupWithViewPager(mViewPager);
        one = mTablayout.getTabAt(0);
        two = mTablayout.getTabAt(1);
        //three = mTablayout.getTabAt(2);
        //one.setIcon(getResources().getDrawable(R.drawable.a));
        //two.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        //setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == mTablayout.getTabAt(0)) {

                    //fab.setVisibility(View.VISIBLE);
                    //one.setIcon(getResources().getDrawable(R.drawable.a));
                    mViewPager.setCurrentItem(0);
                } else if (tab == mTablayout.getTabAt(1)) {
                    //two.setIcon(getResources().getDrawable(R.drawable.a));
                    //fab.setVisibility(View.INVISIBLE);
                    mViewPager.setCurrentItem(1);
                } else if (tab == mTablayout.getTabAt(2)) {
                    // three.setIcon(getResources().getDrawable(R.drawable.a));
                   // fab.setVisibility(View.INVISIBLE);
                    mViewPager.setCurrentItem(2);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == mTablayout.getTabAt(0)) {
                    //one.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                } else if (tab == mTablayout.getTabAt(1)) {
                    //two.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                } else if (tab == mTablayout.getTabAt(2)) {
                    // three.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView =navigationView.getHeaderView(0);
        username_text=(TextView)headerView.findViewById(R.id.Username_tw);
        user_view=(CircleImageView)headerView.findViewById(R.id.user_iv);
        exit_btn=(Button)headerView.findViewById(R.id.nav_exit_btn);
        login_btn=(Button)headerView.findViewById(R.id.login_btn);
        exit_btn.setOnClickListener(new MyOnclickListener());
        login_btn.setOnClickListener(new MyOnclickListener());
        user_iv=(ImageView)headerView.findViewById(R.id.imageview_user);
        navheader_bgp_iv=(ImageView)headerView.findViewById(R.id.imageview_user);
        ViewTreeObserver vto = navheader_bgp_iv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                navheader_bgp_iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Bitmap bitmap= BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                        R.drawable.a,
                        navheader_bgp_iv.getWidth(),
                        navheader_bgp_iv.getHeight());
                navheader_bgp_iv.setImageBitmap(bitmap);
            }
        });
        if(!User.get_login_status()){
            username_text.setVisibility(View.INVISIBLE);
            user_view.setVisibility(View.INVISIBLE);
            exit_btn.setVisibility(View.INVISIBLE);
        }
        else{
            login_btn.setVisibility(View.INVISIBLE);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onRestart() {

        super.onRestart();
        Log.v("------> ", "onRestart");
    }
    @Override
    protected void onResume() {

        super.onResume();
        Log.v("------>", "onResume()");
    }

    class MyOnclickListener implements View.OnClickListener
    {

        @Override
        public void onClick(final View view) {
            switch (view.getId())
            {
                case R.id.login_btn://登录
                    Log.v("------>", "11");
                    final View registerview = getLayoutInflater().inflate(R.layout.activity_login, null);
                    final AppBarLayout appBarLayout=(AppBarLayout)registerview.findViewById(R.id.app_bar);
                    ViewTreeObserver vto = appBarLayout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                                    R.drawable.title,
                                    navheader_bgp_iv.getWidth(),
                                    navheader_bgp_iv.getHeight());
                            Drawable drawable =new BitmapDrawable(bitmap);
                            appBarLayout.setBackground(drawable);
                        }
                    });
                    final TextInputLayout textInputLayout_username = (TextInputLayout) registerview.findViewById(R.id.textInputLayout_username);
                    final TextInputLayout textInputLayout_password = (TextInputLayout) registerview.findViewById(R.id.textInputLayout_password);
                    final EditText editText_dialog_usernmae = textInputLayout_username.getEditText();
                    final EditText editText_dialog_password = textInputLayout_password.getEditText();
                    final ButtonRectangle button_dialog_enter = (ButtonRectangle) registerview.findViewById(R.id.login_button_enter);
                    final ImageButton button_dialog_visibility=(ImageButton)registerview.findViewById(R.id.button_visibility);
                    FloatingActionButton fab = (FloatingActionButton) registerview.findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.v("---->", "2");
                            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                            startActivity(intent);
                        }
                    });
                //    ((ViewGroup) view.getParent()).removeView(view);
                    assert editText_dialog_usernmae != null;
                    editText_dialog_usernmae.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!tools.isEmail(editText_dialog_usernmae.getText().toString())) {
                                // mToast.makeText(registerview.this, "请输入正确的邮箱", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                    assert editText_dialog_password != null;
                    editText_dialog_password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(registerview);



                    final AlertDialog dialog = builder.show();
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Message message =new Message();
                            message.what=1;
                            handler.sendMessage(message);
                            Log.v("关闭","关闭");
                        }
                    });
                    editText_dialog_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    button_dialog_visibility.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if(editText_dialog_password.getInputType()==InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                editText_dialog_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                button_dialog_visibility.setBackground(getResources().getDrawable(R.drawable.ic_visibility_off_grey600_48dp));
                            }
                            else if(editText_dialog_password.getInputType()==(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                                editText_dialog_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                button_dialog_visibility.setBackground(getResources().getDrawable(R.drawable.ic_visibility_grey600_48dp));
                            }
                        }
                    });
                    button_dialog_enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.login_button_enter:

                                    final Thread thread = new Thread() {

                                        public void run() {
                                            try {
                                                Log.v("xx", "fadsfasd");
                                                Message message = new Message();
                                                Tools tools=new Tools(getString(R.string.ipwireless));
                                                if (tools.isUser(editText_dialog_usernmae.getText().toString(), editText_dialog_password.getText().toString())) {
                                                    Snackbar.make(registerview, "登录成功", Snackbar.LENGTH_SHORT).show();

                                                    User.set_login_status(true);
                                                    Thread.sleep(400);
                                                    dialog.dismiss();

                                                    message.what = 0;
                                                    handler.sendMessage(message);
                                                } else {
                                                    Snackbar.make(registerview, "登录失败", Snackbar.LENGTH_LONG).show();
                                                    //message.obj=view;
                                                    User.set_login_status(false);
                                                    message.what = 1;
                                                    handler.sendMessage(message);
                                                }
                                            } catch (Exception e) {
                                                //TODO: handle exception
                                            }
                                        }
                                    };
                                    thread.start();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    break;
                case R.id.nav_exit_btn:
                    final AlertDialog.Builder exit_builder = new AlertDialog.Builder(MainActivity.this);
                    exit_builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Message message =new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    }).setMessage("确定退出登录!").show();
                default:
                    break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_hotel_book) {
            Intent intent = new Intent(MainActivity.this, hotel_book_Activity.class);
            intent.putExtra("Url","http://hotel.openspeech.cn/?app_id=56d80d7a");
            startActivity(intent);
            Log.v("intent","success");
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(MainActivity.this, hotel_book_Activity.class);
            intent.putExtra("Url","http://map.baidu.com/mobile/webapp/third/transit/force=superman/%3Fthird_party=webapp-aladdin&ala_tpl=bus_general&ala_item=linesearch&qid=2011425220&dgr=3&tab=bus");
            startActivity(intent);
            Log.v("intent","success");
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_share) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, "欢迎下载考务系统，http://beta.qq.com/m/2s08 http://dl.downloader-apk.com/apps/2016/02/29/Clash%20Royale%201.2.0_[downloader-apk.com].apk");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "寒亦唱"));
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(MainActivity.this,
                            new Pair<View, String>(user_iv,"image")
                           );
            startActivity(intent,options.toBundle());
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0: //登录成功
                        Log.v(User.get_userid(), User.get_username());
                        username_text.setText(User.get_username());
                        username_text.setVisibility(View.VISIBLE);
                        user_view.setVisibility(View.VISIBLE);
                        exit_btn.setVisibility(View.VISIBLE);
                        login_btn.setVisibility(View.INVISIBLE);
<<<<<<< HEAD
                        ImageRequest header_photo_request= new ImageRequest("http://facedetect.bj.bcebos.com/pic/"+User.get_userid()+".jpg", new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                user_view.setImageBitmap(bitmap);
                            }
                        },240,240, Bitmap.Config.RGB_565,new MyErrorListener());
                        mQueue.add(header_photo_request);
=======
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
                        if(User.get_user_power()==2) {
                            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                                private String[] mTitles = new String[]{"首页", "用户", "考场"};

                                @Override
                                public Fragment getItem(int position) {
                                    if (position == 0) {
                                        return new OneFragment();
                                    } else if (position == 1) {
<<<<<<< HEAD
                                        return new SecondFragment();
                                    } else if (position == 2) {
                                        return new ThirdFragment();
=======
                                        return new ThirdFragment();
                                    } else if (position == 2) {
                                        return new SecondFragment();
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
                                    }
                                    return new OneFragment();
                                }

                                @Override
                                public int getCount() {
                                    return mTitles.length;
                                }

                                @Override
                                public CharSequence getPageTitle(int position) {
                                    return mTitles[position];
                                }
                            });
                            mTablayout.setupWithViewPager(mViewPager);
                            //   mViewPager.notifyAll();
                        } else if(User.get_user_power()==1) {
                            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
<<<<<<< HEAD
                                private String[] mTitles = new String[]{"首页", "我的考证", "用户"};
=======
                                private String[] mTitles = new String[]{"首页", "用户", "我的考证"};
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5

                                @Override
                                public Fragment getItem(int position) {
                                    if (position == 0) {
                                        return new OneFragment();
                                    } else if (position == 1) {
<<<<<<< HEAD
                                        return new SecondFragment();
                                    } else if (position == 2) {
                                        return new ThirdFragment();
=======
                                        return new ThirdFragment();
                                    } else if (position == 2) {
                                        return new SecondFragment();
>>>>>>> b08f28a1c569761438342882ebd8f6d8bca9abc5
                                    }
                                    return new OneFragment();
                                }

                                @Override
                                public int getCount() {
                                    return mTitles.length;
                                }

                                @Override
                                public CharSequence getPageTitle(int position) {
                                    return mTitles[position];
                                }
                            });
                            mTablayout.setupWithViewPager(mViewPager);
                            //   mViewPager.notifyAll();

                        }
                        break;
                    case 1://登录失败
                        login_btn.setVisibility(View.VISIBLE);
                        username_text.setVisibility(View.INVISIBLE);
                        user_view.setVisibility(View.INVISIBLE);
                        exit_btn.setVisibility(View.INVISIBLE);


                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("---->", "MainActivity.onActivityResult");
		Log.v(requestCode + "", resultCode + "");
		if (requestCode == REQUEST_CAMERA_IMAGE || requestCode == REQUEST_CROP_IMAGE) {
			fragmentManager = getSupportFragmentManager();
			Fragment fragment_second = fragmentManager.findFragmentByTag(User.secondFragmentTag);
			fragment_second.onActivityResult(requestCode, resultCode, data);
		}
	}
}
