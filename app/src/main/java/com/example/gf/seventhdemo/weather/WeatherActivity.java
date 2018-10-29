package com.example.gf.seventhdemo.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gf.seventhdemo.R;
import com.example.gf.seventhdemo.common.TopBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity {

    private TopBar topBar;
    private ArrayList<HashMap<String, String>> cityList;
    private SimpleAdapter adapter;
    private Spinner spinner;
    private TextView tv_weather_cond;
    private TextView tv_weather_tmp;
    private String weatherCond;
    private String weatherTmp;
    private String url_city = "https://search.heweather.com/top?group=cn&key=0c37b7efd1d043ccaddc26a5db22ae0c";
    private String url_weather = "https://free-api.heweather.com/s6/weather/now?key=0c37b7efd1d043ccaddc26a5db22ae0c&location=";
    private int url_type;//0:表示请求热门城市列表；1：表示请求某个城市的天气

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityList = new ArrayList<HashMap<String, String>>();
        topBar = new TopBar(WeatherActivity.this);
        spinner = (Spinner) findViewById(R.id.sp_weather_city);
        tv_weather_cond = (TextView) findViewById(R.id.tv_weather_cond);
        tv_weather_tmp = (TextView) findViewById(R.id.tv_weather_tmp);
        initTopBar();//设置标题栏
        sendRequestWithHttpURLConnection(url_city, 0);  //发送HTTP请求
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityList.size() > 0) {
                    HashMap<String, String> city = cityList.get(position);
                    String cid = city.get("cid");
                    Log.d("MSG","***"+url_weather + cid+"***");
                    sendRequestWithHttpURLConnection(url_weather + cid, 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initTopBar() {
        topBar.setTitleName("天 气");
        topBar.setMenuVisible(View.INVISIBLE);
    }

    private void sendRequestWithHttpURLConnection(final String address, final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);   //设置连接超时
                    connection.setReadTimeout(8000);   //设置读取时的毫秒数
                    InputStream in = connection.getInputStream();
                    //对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    handleResponse(response.toString(), type);
                    updateView(type);
                } catch (Exception e) {

                }

            }
        }).start();
    }

    private void updateView(final int type) {
        //返回主线程， 更新UI元素
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                switch (type) {
                    case 0:
                        if (cityList.size() > 0) {
                            adapter = new SimpleAdapter(WeatherActivity.this, cityList, android.R.layout.simple_list_item_1, new String[]{"cname"}, new int[]{android.R.id.text1});
                            spinner.setAdapter(adapter);
                        }
                        break;
                    case 1:
                        tv_weather_cond.setText(weatherCond);
                        tv_weather_tmp.setText(weatherTmp);
                }

            }
        });

    }

    private void handleResponse(String response, int type) {
        switch (type) {
            case 0:
                try {
                    JSONObject result = new JSONObject(response);
                    if (result != null) {
                        JSONArray jsonArray = (JSONArray) result.get("HeWeather6");
                        result = jsonArray.getJSONObject(0);
                        if (result.get("status").equals("ok")) {
                            jsonArray = (JSONArray) result.getJSONArray("basic");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String cid = jsonArray.getJSONObject(i).getString("cid");
                                String cname = jsonArray.getJSONObject(i).getString("location");
                                HashMap<String, String> city = new HashMap<String, String>();
                                city.put("cid", cid);
                                city.put("cname", cname);
                                cityList.add(city);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject result = new JSONObject(response);
                    if (result != null) {
                        JSONArray jsonArray = (JSONArray) result.get("HeWeather6");
                        result = jsonArray.getJSONObject(0);
                        if (result.get("status").equals("ok")) {
                            result = result.getJSONObject("now");
                            weatherCond = result.getString("cond_txt");
                            weatherTmp = result.getString("tmp");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
