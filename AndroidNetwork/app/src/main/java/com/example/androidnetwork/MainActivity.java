package com.example.androidnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GeneratedAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.androidnetwork.adapter.GetresultListAdapter;
import com.example.androidnetwork.domain.GetTextItem;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private GetresultListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.result_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
                outRect.top = 5;
                outRect.bottom= 5;
            }
        });
        adapter = new GetresultListAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void load_Json(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.0.103:9102/get/text");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(1000);//设置时间
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Language", "zh-CN,zh;q =0.9");
                    connection.setRequestProperty("Accept-Enconding", "gzip,deflate");
                    connection.setRequestProperty("Accept", "*/*");
                    connection.connect();
                    //结果码
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        Map<String, List<String>> headerField = connection.getHeaderFields();
                        Set<Map.Entry<String, List<String>>> entries = headerField.entrySet();
                        for (Map.Entry<String, List<String>> entry : entries) {
                            Log.e(TAG, "load_Json: " + entry.getKey() + " ==" + entry.getValue());
                        }
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String json = bufferedReader.readLine();
                        Gson gson = new Gson();
                        GetTextItem getTextItem = gson.fromJson(json, GetTextItem.class);
                        updateUI(getTextItem);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void updateUI(final GetTextItem getTextItem) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setData(getTextItem);
            }
        });
    }
}