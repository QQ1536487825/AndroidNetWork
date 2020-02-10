package com.example.androidnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.androidnetwork.domain.CommentItem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostTestActivity extends AppCompatActivity {
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_test);
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

    public void postFile(View view) {
    }
}
