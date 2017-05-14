package com.phoenix.credit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.common.PhoenixApplication;
import com.phoenix.credit.utils.BitmapUtils;
import com.phoenix.credit.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.phoenix.credit.utils.BitmapUtils.saveImage;

public class UserInfoActivity extends BaseActivity {
    private static final int PICTURE = 100;
    private static final int CAMERA = 200;
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.iv_user_icon)
    ImageView ivUserIcon;
    @BindView(R2.id.btn_user_logout)
    Button btnUserLogout;
    @BindView(R2.id.tv_user_change)
    TextView tvUserChange;

    @Override
    protected void initData() {
        Bitmap bitmap = BitmapUtils.readImage();
        if (bitmap != null){
            ivUserIcon.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R2.id.iv_title_back)
    public void back(View view){
        //销毁当前页面
        this.removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @OnClick(R2.id.btn_user_logout)
    public void logout(View view){//退出登录Button的回调方法
        //1.将保存在SP中的数据清除
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        sp.edit().clear().commit();//清除数据操作必须提交；提交以后，文件仍存在，只是文件中的数据被清空
        //2.将本地保存的图片的File删除
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            filesDir = getExternalFilesDir("");
        }else {
            filesDir = getFilesDir();
        }
        File file = new File(filesDir, "tx.png");
        if (file.exists()){
            file.delete();//删除存储中的文件
        }
        //3.销毁所有的Activity
        this.removeAll();
        //4.重新进入首页面
        this.goToActivity(MainActivity.class, null);
    }

    @OnClick(R2.id.tv_user_change)
    public void changeIcon(View view) {
        String[] items = new String[]{"图库", "相机"};
        //提供一个AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("选择来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://图库
                                //启动其他应用的Activity：使用隐式意图
                                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(picture, PICTURE);
                                break;
                            case 1://相机
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camera, CAMERA);
                                break;
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    //重写启动新的Activity以后的回调方法

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null){//相机
            //获取Intent中的图片对象
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            //对获取到的Bitmap进行压缩、圆形处理
            Bitmap zoomBitmap = BitmapUtils.zoom(bitmap, ivUserIcon.getWidth(), ivUserIcon.getHeight());
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

            //加载显示
            ivUserIcon.setImageBitmap(circleImage);

            //上传到服务器
            uploadImage(zoomBitmap);

            //保存到本地
            saveImage(zoomBitmap);
        }else if(requestCode == PICTURE && resultCode == RESULT_OK && data != null){//图库
            //图库
            Uri selectedImage = data.getData();
            //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,
            //所以要保证无论是哪个系统版本都能正确获取到图片资源的话就需要针对各种情况进行一个处理了
            //这里返回的uri情况就有点多了
            //在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....
            //在4.4.2返回的是content://com.android.providers.media.documents/document/image

            String pathResult = getPath(selectedImage);
//            Log.e("TAG", "onActivityResult--------->pathResult:" + pathResult);
            //存储--->内存
            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, ivUserIcon.getWidth(),ivUserIcon.getHeight());
            //bitmap圆形裁剪
            Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);

            //加载显示
            ivUserIcon.setImageBitmap(circleImage);

            //上传到服务器
            uploadImage(zoomBitmap);

            //保存到本地
            BitmapUtils.saveImage(zoomBitmap);
        }
    }

    private void uploadImage(Bitmap bitmap) {
        // 将bitmap转为string，并使用BASE64加密
        String photo = BitmapUtils.BitmapToString(bitmap);
        // 获取到图片的名字
//        String name = photoPath.substring(photoPath.lastIndexOf("/")).substring(1);
        String name = "tx.png";
        // new一个请求参数
        RequestParams params = new RequestParams();
        // 将图片和名字添加到参数中
        params.put("photo", photo);
        params.put("name", name);
        AsyncHttpClient client = new AsyncHttpClient();
        // 调用AsyncHttpClient的post方法
        client.post(AppNetConfig.UPLOAD, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "上传成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Toast.makeText(getApplicationContext(), "上传失败!", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure--------->上传失败" + arg3.getMessage().toString());
            }
        });
    }

    /**
     * 根据系统相册选择的文件获取路径
     * @param uri
     */
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
//        Log.e("TAG", "getPath--------->" + uri.getPath());
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
//            Log.e("TAG", "高于4.4.2的版本-->uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
//                Log.e("TAG", "getPath--------->isExternalStorageDocument");
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
//                Log.e("TAG", "getPath--------->isDownloadsDocument");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
//                Log.e("TAG", "getPath--------->isMediaDocument");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
//                Log.e("TAG", "getPath--------->isMedia");

//                String[] proj = {MediaStore.Images.Media.DATA};
//                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                String[] filePathColumn = {MediaStore.MediaColumns.DATA};
                Cursor actualimagecursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
            //如果再更新版本，继续在此判断
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
