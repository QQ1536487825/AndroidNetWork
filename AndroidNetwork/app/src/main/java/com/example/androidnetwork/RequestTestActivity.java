package com.example.androidnetwork;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidnetwork.domain.CommentItem;
import com.example.androidnetwork.util.IOUtils;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PortUnreachableException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestTestActivity extends AppCompatActivity {
    private static final String TAG = "";
    String urll = "http://192.168.0.104:9102";
    public static final int PERMIS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);
        int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMIS_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
    }
    public void postsFile(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;
//                BufferedInputStream bfi = null;
//                InputStream inputStream = null;

                try {
                    File fileTwo = new File("/storage/emulated/0/13.png");
                    File fileTo = new File("/storage/emulated/0/11.png");
//                    File fileThr = new File("/storage/emulated/0/11.jpg");
                    File fileOne = new File("/storage/emulated/0/12.png");
//                    File fileOn = new File("/storage/emulated/0/14.png");

                    String fileKey = "files";
                    String fileType = "image/jpeg";
                    String BOUNDARY = "--------------------------954555323792164398227139";
                    //String BOUNDARY = "----------------------------954555323792164398227139--";
                    //String BOUNDARY = "----------------------------954555323792164398227139";
                    URL url = new URL("http://192.168.0.104:9102/files/upload");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setRequestProperty("User-Agent","Android/" + Build.VERSION.SDK_INT);
                    httpURLConnection.setRequestProperty("Accept","*/*");
                    httpURLConnection.setRequestProperty("Cache-Control","no-cache");
                    httpURLConnection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
                    httpURLConnection.setRequestProperty("Connection","keep-alive");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //连接
                    httpURLConnection.connect();

                    outputStream = httpURLConnection.getOutputStream();
                    uploadFile(fileOne,fileKey,fileOne.getName(),fileType,BOUNDARY,outputStream,false);
//                    uploadFile(fileThr,fileKey,fileThr.getName(),fileType,BOUNDARY,outputStream,false);
//                    uploadFile(fileOn,fileKey,fileOn.getName(),fileType ,BOUNDARY,outputStream,false);
                    uploadFile(fileTo,fileKey,fileTo.getName(),fileType,BOUNDARY,outputStream,false);
                    uploadFile(fileTwo,fileKey,fileTwo.getName(),fileType,BOUNDARY,outputStream,true);
                    outputStream.flush();
                    //获取返回的结果
                    int responseCode = httpURLConnection.getResponseCode();
                    Log.d(TAG,"responseCode -- > " + responseCode);
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                        String result = bf.readLine();
                        Log.d(TAG,"result -- > " + result);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    if(outputStream != null) {
                        try {
                            outputStream.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
//
            private void uploadFile(File file, String fileKey, String fileName, String fileType,
                                    String BOUNDARY, OutputStream outputStream,boolean isLast) throws IOException {
                //准备数据
                StringBuilder header = new StringBuilder();
                header.append("--");
                header.append(BOUNDARY);
                header.append("\r\n");
                header.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + fileName + "\"");
                header.append("\r\n");
                header.append("Content-Type：" + fileType);
                header.append("\r\n");
                header.append("\r\n");
                byte[] headInfoBytes = header.toString().getBytes("UTF-8");
                outputStream.write(headInfoBytes);

                //文件内容
                FileInputStream fos = new FileInputStream(file);
                BufferedInputStream bfi = new BufferedInputStream(fos);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = bfi.read(buffer, 0, buffer.length)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                //写尾部信息
                StringBuilder footerInfo = new StringBuilder();
                footerInfo.append("\r\n");
                footerInfo.append("--");
                footerInfo.append(BOUNDARY);
                if(isLast) {
                    footerInfo.append("--");
                    footerInfo.append("\r\n");
                }
                footerInfo.append("\r\n");
                outputStream.write(footerInfo.toString().getBytes("UTF-8"));
            }
        }).start();
    }

    public void postFile(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream outputStream = null;
                BufferedInputStream bfi = null;
                InputStream inputStream = null;

                try {
                    File file = new File("/storage/emulated/0/11.png");

                    Log.e(TAG, "run: " + "j1j");
                    String fileKey = "file";
                    String fileName = file.getName();
                    Log.e(TAG, "run: " + fileName);
                    String fileType = "image/png";

                    String BOUNDARY = "--------------------------954555323792164398227139";

                    URL url = new URL("http://192.168.0.104:9102/file/upload");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(1000);
                    connection.setRequestProperty("User-Agent", "Android/" + Build.VERSION.SDK_INT);
                    connection.setRequestProperty("Accept", "*/*");
                    connection.setRequestProperty("Cache-Control", "no-cache");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                    connection.setRequestProperty("Connection", "keep-alive");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    //连接
                    connection.connect();
                    Log.e(TAG, "run: " + "jj");
                      outputStream = connection.getOutputStream();
                    StringBuilder header = new StringBuilder();
                    header.append("--");
                    header.append(BOUNDARY);
                    header.append("\r\n");
                    header.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + fileName + "\"");
                    header.append("\r\n");
                    header.append("Content-Type：" + fileType);
                    header.append("\r\n");
                    header.append("\r\n");
                    byte[] headInfoBytes = header.toString().getBytes("UTF-8");
                    outputStream.write(headInfoBytes);

                    //文件内容
                    FileInputStream fos = new FileInputStream(file);
                      bfi = new BufferedInputStream(fos);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = bfi.read(buffer, 0, buffer.length)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    //写尾部信息
                    StringBuilder footerInfo = new StringBuilder();
                    footerInfo.append("\r\n");
                    footerInfo.append("--");
                    footerInfo.append(BOUNDARY);
                    footerInfo.append("--");
                    header.append("\r\n");
                    header.append("\r\n");
                    outputStream.write(footerInfo.toString().getBytes("UTF-8"));
                    outputStream.flush();

                    //获取返回的结果
                    int responseCode = connection.getResponseCode();
                    Log.e(TAG, "run11: " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                          inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String s = bufferedReader.readLine();
                        Log.e(TAG, "run11: " + s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
                if (bfi!=null) {
                    try {
                        bfi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void postWithParams(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "这是我的字符串");
        params.put("page", "12");
        params.put("order", "0");
        startRequest(params, "POST", "/post/string");

    }

    public void getWithParams(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "这是我的关键字");
        params.put("page", "12");
        params.put("order", "0");
        startRequest(params, "GET", "/get/param");

    }

    private void startRequest(final Map<String, String> params, final String method, final String api) {

        new Thread(new Runnable() {


            @Override
            public void run() {

                BufferedReader bufferedReader = null;
                try {
                    //组装参数
                    StringBuilder stringBuilder = new StringBuilder();

                    if (params != null && params.size() > 0) {
                        stringBuilder.append("?");
                        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> next = iterator.next();
                            stringBuilder.append(next.getKey());
                            stringBuilder.append("=");
                            stringBuilder.append(next.getValue());
                            if (iterator.hasNext()) {
                                stringBuilder.append("&");

                            }
                        }
                        Log.d(TAG, "run1: " + stringBuilder.toString());

                    }
                    String params = stringBuilder.toString();
                    URL url;
                    if (params != null && params.length() > 0) {
                        url = new URL(urll + api + params);
                    } else {
                        url = new URL(urll + api);
                    }
                    Log.e(TAG, "run: " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
                    connection.setRequestProperty("Accept", "application/json,text/plain,*/*");
                    connection.connect();
                    //拿结果
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        InputStream inputStream = connection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String json = bufferedReader.readLine();
                        Log.e(TAG, "run2: " + json);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }).start();
    }


    public void postRequest(View view) {
        new Thread(new Runnable() {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.0.104:9102/post/comment");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(1000);
                    connection.setRequestProperty("Content-Type", "application/json;Charset = UTF-8");
                    connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
                    connection.setRequestProperty("Accept", "application/json,text/plain,*/*");
                    CommentItem commentItem = new CommentItem("1233", "我是评论内容");
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(commentItem);
                    byte[] bytes = jsonStr.getBytes("UTF-8");
                    connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                    //连接
                    connection.connect();
                    //把数据给到服务器
                    outputStream = connection.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.flush();
                    //拿结果
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        Log.e(TAG, "run: " + bufferedReader.readLine());
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void downLoad(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fileOutputStream = null;
                InputStream inputStream= null;

                try {
                    URL url = new URL(urll+"/download/10");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(1000);//设置时间
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Language", "zh-CN,zh;q =0.9");
                    connection.setRequestProperty("Accept-Enconding", "gzip,deflate");
                    connection.setRequestProperty("Accept", "*/*");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode== HttpURLConnection.HTTP_OK) {
                        String headerField = connection.getHeaderField("Content-disposition");
                        Log.d(TAG, "run: header"+headerField);
                        /*方法一，截取文件名称*/
//                        int index = headerField.indexOf("filename=");
//                        String fileName = headerField.substring(index + "filename=".length());
//                        Log.d(TAG, "run: fileName"+fileName);
                        /*方法二，截取文件名称*/
                        String fileName = headerField.replace("attachment; filename=", "");
                        Log.d(TAG, "run: filename:"+fileName);

                        File picFile = RequestTestActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        if (!picFile.exists()) {
                            picFile.mkdirs();

                        }
                        //创建File
                        File file = new File(picFile+File.separator+fileName);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        Log.d(TAG, "run: file"+file);
                        fileOutputStream = new FileOutputStream(file);
                        inputStream = connection.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                            fileOutputStream.write(buffer,0,len);

                        }
                        fileOutputStream.flush();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {

                    IOUtils.isClose(inputStream);
                    IOUtils.isClose(fileOutputStream);
                }
            }
        }).start();
    }
}
