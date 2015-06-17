package com.coolweather.app.util;

/**
 * Created by Aeolia on 2015/6/17.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
