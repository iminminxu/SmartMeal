package com.ecare.smartmeal.model.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.ecare.smartmeal.BuildConfig;
import com.ecare.smartmeal.config.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/11.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * ProjectApi
 */
public class ProjectApi {

    //apiService
    private ProjectApiService apiService;
    //默认超时时间
    private static final int DEFAULT_TIMEOUT = 15;

    private ProjectApi() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)               //失败重试（根据情况配置是否设置true）
                .addInterceptor(getRequestInterceptor())      //设置Header,Url
                .addInterceptor(getHttpLoggingInterceptor()); //请求日志打印（应在debug下打印）

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())        //添加Gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //支持RxJava2
                .client(httpClientBuilder.build())
                .build();

        apiService = retrofit.create(ProjectApiService.class);
    }

    public static ProjectApi getInstance() {
        return ProjectApiHolder.projectApi;
    }

    private static class ProjectApiHolder {
        private static final ProjectApi projectApi = new ProjectApi();
    }

    /**
     * 日志输出
     *
     * @return HttpLoggingInterceptor
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.d("RxRetrofit", "Retrofit====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    /**
     * 请求加header
     *
     * @return Interceptor
     */
    private Interceptor getRequestInterceptor() {
        return chain -> {
            Request original = chain.request();
            //设置Header,Url
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Connection", "close")
                    .header("Authorization", SPUtils.getInstance(Constants.SP_USER).getString(Constants.SP_TOKEN));
            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    /**
     * 获取ApiService
     *
     * @return ProjectApiService
     */
    public ProjectApiService getApiService() {
        return apiService;
    }
}
