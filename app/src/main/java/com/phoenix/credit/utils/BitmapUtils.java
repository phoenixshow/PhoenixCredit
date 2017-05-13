package com.phoenix.credit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.Base64;

import com.phoenix.credit.common.PhoenixApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by flashing on 2017/5/11.
 */

public class BitmapUtils {
    /**
     * Bitmap的圆形处理
     * @param source
     * @return
     */
    public static Bitmap circleBitmap(Bitmap source){
        //获取Bitmap的宽度
        int width = source.getWidth();
        //以Bitmap的宽度值作为新的Bitmap的宽高值
        Bitmap bitmap = createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //以此Bitmap为基准，创建一个画布
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //在画布上画一个圆，作为下层//Bitmap作为上层，与圆相交
        canvas.drawCircle(width/2, width/2, width/2, paint);
        /**
         * 设置图片相交情况下的处理方式
         * setXfermode设置当绘制的图像出现相交情况时候的处理方式的，它包含的常用模式有：
         *      PorterDuff.Mode.SRC_IN取两层图像交集部分，只显示上层图像
         *      PorterDuff.Mode.DST_IN取两层图像交集部分，只显示下层图像
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //在画布上绘制Bitmap
        canvas.drawBitmap(source, 0, 0, paint);
        return bitmap;
    }

    //实现图片的压缩处理//设置宽高必须使用浮点型，否则导致压缩的比例为0
    public static Bitmap zoom(Bitmap source, float width, float height){
        Matrix matrix = new Matrix();
        //图片的压缩处理
        matrix.postScale(width / source.getWidth(), height / source.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        return bitmap;
    }

    // 将bitmap转成string类型通过Base64
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        // 将bitmap压缩成30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bao);
        // 将bitmap转化为一个byte数组
        byte[] bs = bao.toByteArray();
        // 将byte数组用BASE64加密
        String photoStr = Base64.encodeToString(bs, Base64.DEFAULT);
        // 返回String
        return photoStr;
    }

    public static Bitmap readImage() {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            filesDir = PhoenixApplication.context.getExternalFilesDir("");
        }else {
            filesDir = PhoenixApplication.context.getFilesDir();
        }
        File file = new File(filesDir, "tx.png");
        if (file.exists()){
            //存储-->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //圆形处理
            bitmap = BitmapUtils.circleBitmap(bitmap);
            return bitmap;
        }
        return null;
    }
}
