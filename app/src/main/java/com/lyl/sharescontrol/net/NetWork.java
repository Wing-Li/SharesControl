package com.lyl.sharescontrol.net;

import com.lyl.sharescontrol.net.api.ShareApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by lyl on 2017/5/15.
 */

public class NetWork {

    private static String URL_NEIHAN = "http://hq.sinajs.cn/";

    private static final int DEFAULT_TIMEOUT = 30;

    private static OkHttpClient.Builder httpClientBuilder;
    private static ShareApi shareApi;

    private static void initOkHttp() {
        httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    private static Retrofit getRetrofit(String url) {
        if (httpClientBuilder == null) {
            initOkHttp();
        }

        return new Retrofit.Builder()//
                .client(httpClientBuilder.build())//
                .baseUrl(url)//
                .addConverterFactory(ScalarsConverterFactory.create())//
                .build();
    }

    public static ShareApi getShareApi() {
        if (shareApi == null) {
            shareApi = getRetrofit(URL_NEIHAN).create(ShareApi.class);
        }
        return shareApi;
    }
}
