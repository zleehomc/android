
package com.android.hyc.hyc_final;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import Face.FaceUtil;


public class Facedetect extends AppCompatActivity {
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private final int REQUEST_CAMERA_IMAGE = 2;

    private Bitmap mImage = null;
    private byte[] mImageData = null;
    // authid为6-18个字符长度，用于唯一标识用户
    private String mAuthid = null;
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facedetect);
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        Button btn_camera =(Button)findViewById(R.id.btn_online_camera);
        Button btn_reg=(Button)findViewById(R.id.btn_online_reg);
        Button btn_pick=(Button)findViewById(R.id.btn_online_pick);
        Button btn_verify=(Button)findViewById(R.id.btn_online_verify);
        // ImageView imageView=(ImageView)findViewById(R.id.imageView);
        btn_reg.setOnClickListener(new My_OnClickListener());
        btn_camera.setOnClickListener(new My_OnClickListener());
        btn_pick.setOnClickListener(new My_OnClickListener());
        btn_verify.setOnClickListener(new My_OnClickListener());


        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {  // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });

        mFaceRequest = new FaceRequest(this);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });








    }
    @Override
    protected void onResume() {
        super.onResume();
        FlowerCollector.onPageStart("人脸识别");
        FlowerCollector.onResume(this);
    }
    @Override
    protected void onPause() {
        FlowerCollector.onPageEnd("人脸识别");
        FlowerCollector.onPause(this);
        FlowerCollector.flush(this);
        super.onPause();


    }
    class My_OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_online_camera:
                    mPictureFile = new File(Environment.getExternalStorageDirectory(),
                            "picture" + System.currentTimeMillis()/1000 + ".jpg");
                    Log.v("----->","xxx");
                    // 启动拍照,并保存到临时文件
                    Intent mIntent = new Intent();
                    mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                    mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    Log.v("----->", "xxxx");
                    startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
                    break;
                case R.id.btn_online_pick:
                    Log.v("------>","1");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    Log.v("------>", "2");
                    intent.setAction(Intent.ACTION_PICK);
                    Log.v("------>", "3");
                    startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                    Log.v("------>", "4");
                    break;
                case R.id.btn_online_reg:
                    mAuthid = ((EditText) findViewById(R.id.online_editText)).getText().toString();
                    if (TextUtils.isEmpty(mAuthid)) {
                        showTip("authid不能为空");
                        return;
                    }
                    if (null != mImageData) {
                        mProDialog.setMessage("注册中...");
                        mProDialog.show();
                        // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                        // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                        mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                        mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                        mFaceRequest.sendRequest(mImageData, mRequestListener);
                    } else {
                        showTip("请选择图片后再注册");
                    }
                    break;
                case R.id.btn_online_verify:
                    FlowerCollector.onEventEnd(Facedetect.this, "注册");
                    mAuthid = ((EditText) findViewById(R.id.online_editText)).getText().toString();
                    if (TextUtils.isEmpty(mAuthid)) {
                        showTip("authid不能为空");
                        return;
                    }

                    if (null != mImageData) {
                        mProDialog.setMessage("验证中...");
                        mProDialog.show();
                        // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                        // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                        mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                        mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                        mFaceRequest.sendRequest(mImageData, mRequestListener);
                    } else {
                        showTip("请选择图片后再验证");
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            showTip("注册成功");
        } else {
            showTip("注册失败");
        }
    }

    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("验证失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                showTip("通过验证，欢迎回来！");
            } else {
                showTip("验证不通过");
            }
        } else {
            showTip("验证失败");
        }
    }




    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                }
                else if ("verify".equals(type)) {
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("----->", "dft");
        if (resultCode != RESULT_OK) {
            Log.v("------>", "5");
            return;
        }
        Log.v("------>", "6");
        String fileSrc = null;
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            Log.v("------>", "7");
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                Log.v("------>", "8");
                fileSrc = data.getData().getPath();
                Log.v("------>", "9");
            } else {
                // Uri模型为content
                Log.v("------>", "10");
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
                Log.v("------>", "11");
            }
            Log.v("------>", "9.1");
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
            Log.v("------>", "9.2");
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                Log.v("------>", "12");
                return;
            }
            Log.v("------>", "13");
            fileSrc = mPictureFile.getAbsolutePath();
            updateGallery(fileSrc);
            // 跳转到图片裁剪页面
            Log.v("------>", "14");
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            if(data!=null) {
                Bitmap bmp = (Bitmap) data.getParcelableExtra("data");
                // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
                if (null != bmp) {
                    FaceUtil.saveBitmapToFile(Facedetect.this, bmp);
                }
            }

            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(Facedetect.this);
            Log.v("-->", fileSrc);
            // 获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);


            // 若mImageBitmap为空则图片信息不能正常获取
            if (null == mImage) {
                showTip("图片信息无法正常获取！");
                Log.v("------>", "图片信息无法正常获取");
                return;
            }
            Log.v("------>", "图片信息正常获取");
            Log.v("------>", mImage.toString());
            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();

            ((ImageView) findViewById(R.id.online_imgView)).setImageBitmap(mImage);
        }

    }

    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(this, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


}
