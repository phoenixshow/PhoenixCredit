package com.phoenix.credit.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.phoenix.credit.BuildConfig;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.bean.UpdateInfo;
import com.phoenix.credit.common.ActivityManager;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by flashing on 2017/4/28.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final int TO_MAIN = 1;
    private static final int DOWNLOAD_VERSION_SUCCESS = 2;
    private static final int DOWNLOAD_APK_FAIL = 3;
    private static final int DOWNLOAD_APK_SUCCESS = 4;
    @BindView(R2.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @BindView(R2.id.rl_welcome)
    RelativeLayout rlWelcome;
    @BindView(R.id.tv_welcome_version)
    TextView tvWelcomeVersion;

    private long startTime;
    private UpdateInfo updateInfo;
    private ProgressDialog dialog;
    private File apkFile;

    private static int REQUESTPERMISSION = 110 ;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_MAIN:
                    finish();
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    break;
                case DOWNLOAD_VERSION_SUCCESS:
                    //获取当前应用的版本信息
                    String version = getVersion();
                    //更新页面显示的版本信息
                    tvWelcomeVersion.setText(version);
                    //比较服务器获取的最新的版本跟本应用的版本是否一致
                    if (version.equals(updateInfo.version)){
                        UIUtils.toast("当前应用已经是最新版本", false);
                        toMain();
                    }else {
                        new AlertDialog.Builder(WelcomeActivity.this)
                                .setTitle("下载最新版本")
                                .setMessage(updateInfo.desc)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //下载服务器保存的应用数据
                                        downloadApk();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        toMain();
                                    }
                                })
                                .show();
                    }
                    break;
                case DOWNLOAD_APK_FAIL:
                    UIUtils.toast("联网下载数据失败", false);
                    toMain();
                    break;
                case DOWNLOAD_APK_SUCCESS:
                    UIUtils.toast("下载应用数据成功", false);
                    dialog.dismiss();
                    installApk();//安装下载好的应用
                    finish();//结束当前的欢迎界面的显示
                    break;
            }
        }
    };

    private void installApk() {
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
//        intent.setData(Uri.parse("file:" + apkFile.getAbsolutePath()));
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    private void downloadApk() {
        //初始化水平进度条的Dialog
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
        //初始化数据要保持的位置
        File fileDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            fileDir = this.getExternalFilesDir("");
        }else {
            fileDir = this.getFilesDir();
        }
        apkFile = new File(fileDir, "update.apk");

        //启动一个分线程联网下载数据
        new Thread(){
            @Override
            public void run() {
                String path = AppNetConfig.BASE_URL+updateInfo.apkUrl;
                InputStream is = null;
                FileOutputStream fos = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
//                    conn.setReadTimeout(5000);
                    conn.connect();
                    if (conn.getResponseCode() == 200){
                        dialog.setMax(conn.getContentLength());//设置Dialog的最大值
                        is = conn.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = is.read(buffer)) != -1){
                            //更新Dialog的进度
                            dialog.incrementProgressBy(len);
                            fos.write(buffer, 0, len);
                            SystemClock.sleep(1);//延时看下载进度的效果，实际应用中要去掉
                        }
                        handler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);
                    }else {
                        handler.sendEmptyMessage(DOWNLOAD_APK_FAIL);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(DOWNLOAD_APK_FAIL);
                } finally {
                    if (conn != null){
                        conn.disconnect();
                    }
                    if (is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * 当前版本号
     *
     * @return
     */
    private String getVersion() {
        String version = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        return version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去掉窗口标题
//        getSupportActionBar().hide();
        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //将当前的activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);
        //提供启动动画
        setAnimation();

        //联网更新应用
        updateApkFile();
    }

    private void updateApkFile() {
        //获取系统当前时间
        startTime = System.currentTimeMillis();

        //判断手机是否可以联网
        boolean connect = isConnect();
        if (!connect) {//没有移动网络
            UIUtils.toast("当前没有移动数据网络", false);
            toMain();
        } else {//有移动网络
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
            }else{
                getServiceVersion();
            }
        }
    }

    private void getServiceVersion() {
        //联网获取服务器的最新版本数据
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppNetConfig.UPDATE;
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String content = new String(responseBody);
                //解析Json数据
                updateInfo = JSON.parseObject(content, UpdateInfo.class);
                handler.sendEmptyMessage(DOWNLOAD_VERSION_SUCCESS);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.sendEmptyMessage(DOWNLOAD_APK_FAIL);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUESTPERMISSION){
            if(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getServiceVersion();
                }else{
                    UIUtils.toast("提示没有权限，安装不了咯~", false);
                    toMain();
                }
            }
        }
    }

    private void toMain() {
        long currentTime = System.currentTimeMillis();
        long delayTime = 3000 - (currentTime - startTime);
        if (delayTime < 0) {
            delayTime = 0;
        }
        handler.sendEmptyMessageDelayed(TO_MAIN, delayTime);
    }

    private boolean isConnect() {
        boolean connected = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }
        return connected;
    }

    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//0：完全透明  1：完全不透明
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());//设置动画的变化率，可以匀速，也可以变速，这里是加速动画

        /*//方式一：
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            //当动画结束时：调用如下方法
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();//销毁当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
//        //方式二：使用handler
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                startActivity(intent);
////                finish();//销毁当前页面
//                //结束activity的显示，并从栈空间中移除
//                ActivityManager.getInstance().remove(WelcomeActivity.this);
//            }
//        }, 3000);

        //启动动画
        rlWelcome.startAnimation(alphaAnimation);
    }
}
