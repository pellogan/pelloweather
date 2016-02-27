package com.example.pello.pelloweather.util;

/**
 * Created by Pello on 2016/2/23.
 */
public interface HttpCallBackListener {
    void onFinish(String response);

    void onError(Exception e);
}
