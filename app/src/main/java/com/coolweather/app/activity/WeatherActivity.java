package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Aeolia on 2015/6/17.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDescText;
    private TextView tempText;
    private TextView currentDateText;
    private String nowCityName;
    private Button switchCity;
    private Button refreshWeather;
    private String apiKey = "y3FvpwWZa5eVH5PW2hjT8sUK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText = (TextView)findViewById(R.id.publish_text);
        weatherDescText = (TextView)findViewById(R.id.weather_desc);
        tempText = (TextView)findViewById(R.id.temp);
        currentDateText = (TextView)findViewById(R.id.current_date);
        switchCity = (Button)findViewById(R.id.switch_city);
        refreshWeather = (Button)findViewById(R.id.refresh_weather);
        nowCityName = getIntent().getStringExtra("city_name");
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);

        if (!TextUtils.isEmpty(nowCityName)) {
            publishText.setText("同步中……");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            cityNameText.setText(nowCityName);
            queryWeatherInfo(nowCityName);
        } else {
            //showWeather();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city :
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中……");
                queryWeatherInfo(nowCityName);
                break;
            default:
                break;
        }
    }

    private void queryWeatherInfo(String cityName) {
        String address = "http://api.map.baidu.com/telematics/v3/weather?output=json&ak=y3FvpwWZa5eVH5PW2hjT8sUK&"
                + "location=" + URLEncoder.encode(cityName);
        Log.d("weather",address);
        queryFromServer(address, "cityName");
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("cityName".equals(type)) {
                    final JSONObject weatherData =  Utility.handleWeatherResponse(WeatherActivity.this, response);
                    if (weatherData != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather(weatherData);
                            }
                        });
                    }

                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather(JSONObject weatherData) {
        try {
            tempText.setText(weatherData.getString("temperature"));
            publishText.setText("同步成功 "+System.currentTimeMillis());
            weatherDescText.setText(weatherData.getString("weather"));
            currentDateText.setText(weatherData.getString("date"));
            weatherInfoLayout.setVisibility(View.VISIBLE);
            cityNameText.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            publishText.setText("程序错误");
        }

    }
}
