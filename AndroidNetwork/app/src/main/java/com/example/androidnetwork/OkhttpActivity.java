package com.example.androidnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.androidnetwork.domain.CommentItem;
import com.example.androidnetwork.util.IOUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import kotlin.Pair;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkhttpActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.0.104:9102";
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
    }

    public void getRequest(View view) {
        //要有客户端，类似于有一个浏览器
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        //创建请求内容
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL + "/get/text")
                .build();

        //用client去创建请求任务
        Call task = okHttpClient.newCall(request);
        //异步请求
        task.enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "onResponse: " + code);
                    ResponseBody body = response.body();
                    Log.d(TAG, "onResponse: " + body.string());
                }

            }
        });
    }

    public void postComment(View view) {
        //先是有client
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();

        //要提交的内容
        CommentItem commentItem = new CommentItem("1233", "是评论内容");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(commentItem);
        MediaType mediaType = MediaType.parse("application/json");
        final RequestBody requestBody = RequestBody.create(jsonStr, mediaType);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(BASE_URL + "/post/comment")
                .build();

        Call task = client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                Log.d(TAG, "onResponse: " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.d(TAG, "onResponse: " + body.string());
                    }
                }
            }
        });
    }

    //上传单文件
    public void postFile(View view) {
        //先是有client
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();

        File file = new File("/storage/emulated/0/13.png");

        MediaType fileType = MediaType.parse("image/png");
        final RequestBody fileBody = RequestBody.create(file, fileType);
        final RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/file/upload")
                .post(requestBody)
                .build();
        Call task = client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                Log.d(TAG, "onResponse: ");
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        Log.d(TAG, "onResponse: " + string);
                    }
                }
            }
        });
    }

    //上传多文件
    public void postFiles(View view) {
        //先是有client
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();

        File file = new File("/storage/emulated/0/13.png");
        File filetwo = new File("/storage/emulated/0/12.png");
        File filethree = new File("/storage/emulated/0/11.png");

        MediaType fileType = MediaType.parse("image/png");
        RequestBody fileBody = RequestBody.create(file, fileType);
        RequestBody fileoneBody = RequestBody.create(filetwo, fileType);
        RequestBody filethreeBody = RequestBody.create(filethree, fileType);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("files", file.getName(), fileBody)
                .addFormDataPart("files", filetwo.getName(), fileoneBody)
                .addFormDataPart("files", filethree.getName(), filethreeBody)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/files/upload")
                .post(requestBody)
                .build();
        Call task = client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                Log.d(TAG, "onResponse: ");
                if (code == HttpURLConnection.HTTP_OK) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        Log.d(TAG, "onResponse: " + string);
                    }
                }
            }
        });
    }

    //下载文件
    public void downFiles(View view) {
        //先是有client
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL + "/download/14")
                .build();
        final Call call = client.newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    downFile(response.body().byteStream(),response.headers());

                }
            }
        });
//
//        //同步请求
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FileOutputStream fos = null;
//                InputStream inputStream = null;
//                try {
//                    //同步
//                    Response execute = call.execute();
//                    int code = execute.code();
//                    Log.d(TAG, "run: " + code);
//                    if (code == HttpURLConnection.HTTP_OK) {
//
//                        Headers headers = execute.headers();
//                        for (int i = 0; i < headers.size(); i++) {
//                            String name = headers.name(i);
//                            String value = headers.value(i);
//                            Log.d(TAG, "run: " + name + value);
//                        }
//                        InputStream inputStream1 = execute.body().byteStream();
//                        downFile(inputStream1, headers);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


    }

    private void downFile(InputStream inputStream, Headers headers) throws IOException {
        String contentType = headers.get("Content-disposition");
        String fileName = contentType.replace("attachment; filename=", "");
        File outFile = new File(OkhttpActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + fileName);
        Log.d(TAG, "run: outFile." + outFile);
        if (!outFile.getParentFile().exists()) {
            outFile.mkdirs();
        }
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            fos.write(buffer, 0, len);

        }
        IOUtils.isClose(fos);
        IOUtils.isClose(inputStream);
    }

}
