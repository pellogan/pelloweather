package com.example.pello.pelloweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Pello on 2016/2/23.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallBackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(1000 * 10);
                    con.setReadTimeout(1000 * 10);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line ;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());

                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                    e.printStackTrace();
                }finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }

            }
        }).start();


    }

}
