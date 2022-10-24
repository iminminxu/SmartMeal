package com.ecare.smartmeal.base;

import android.os.Bundle;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/3.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * BasePagingView
 */
public interface BasePagingView extends BaseView {

    void setNoMoreData();

    /**
     * 跳转界面
     * {@link #startActivity(Class, Bundle)}的默认实现
     *
     * @param clz 跳转的目的Activity类
     */
    default void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转界面
     *
     * @param clz    跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    void startActivity(Class<?> clz, Bundle bundle);
}