package com.phoenix.credit.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class UploadUtil {

    private static final int TIME_OUT = 10 * 1000;
    private static final String CHARSET = "UTF-8";

    public static int uploadFile(File file, String requestURL) {
        int res = 0;

        String BOUNDARY = UUID.randomUUID().toString();//边界分界符
        String PREFIX = "--";//连字符
        String LINE_END = "\r\n";//换行符
        String CONTENT_TYPE = "multipart/form-data";//MIME类型

        try {
            URL url = new URL(requestURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(TIME_OUT);
            httpURLConnection.setConnectTimeout(TIME_OUT);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Charset", CHARSET);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");//长连接
            httpURLConnection.setRequestProperty("Content-type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if (file != null) {
                Log.e("TAG", "upload start");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; Charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                //写入前缀
                dataOutputStream.write(sb.toString().getBytes());
                //开始写文件
                InputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bytes)) != -1) {
                    dataOutputStream.write(bytes, 0, len);
                    Log.e("TAG", "upload bytes:" + len);
                }
                inputStream.close();
                dataOutputStream.write(LINE_END.getBytes());
                //写入结束符
                String end = PREFIX + BOUNDARY + PREFIX + LINE_END;
                dataOutputStream.write(end.getBytes());
                dataOutputStream.flush();
                Log.e("TAG", "upload end");
                //得到服务器的响应
                if (httpURLConnection.getResponseCode() == 200){
                    Log.e("TAG", "upload success");
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer stringBuffer = new StringBuffer();
                    String readLine = "";
                    while ((readLine = bufferedReader.readLine()) != null){
                        stringBuffer.append(readLine);
                    }
                    is.close();
                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    Log.e("TAG", "result:" + stringBuffer.toString());
                }else {
                    Log.e("TAG", "upload fail");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
