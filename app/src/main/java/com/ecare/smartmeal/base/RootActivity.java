package com.ecare.smartmeal.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ecare.smartmeal.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/11.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * RootActivity
 */
public abstract class RootActivity<T extends BasePresenter> extends BaseActivity<T> {

    private static final int STATE_LOADING = 0x00;
    private static final int STATE_MAIN = 0x01;
    private static final int STATE_ERROR = 0x02;

    private int currentState = STATE_MAIN;

    private View viewLoading;
    private ViewGroup viewMain;
    private View viewError;

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        viewMain = findViewById(R.id.view_main);
        if (viewMain == null) {
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain a View named 'viewMain'.");
        }
        if (!(viewMain.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "viewMain's ParentView should be a ViewGroup.");
        }
        ViewGroup mParent = (ViewGroup) viewMain.getParent();
        //设置加载页
        View.inflate(mContext, R.layout.view_loading, mParent);
        viewLoading = mParent.findViewById(R.id.view_loading);
        //设置错误页
        View.inflate(mContext, R.layout.view_error, mParent);
        viewError = mParent.findViewById(R.id.view_error);
        viewError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoadDataFailRetry();
            }
        });
        //设置默认显示
        viewLoading.setVisibility(View.GONE);
        viewMain.setVisibility(View.VISIBLE);
        viewError.setVisibility(View.GONE);
    }

    protected abstract void firstLoadDataFailRetry();

    private void hideCurrentView() {
        switch (currentState) {
            case STATE_LOADING:
                viewLoading.setVisibility(View.GONE);
                break;
            case STATE_MAIN:
                viewMain.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                viewError.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void stateLoading() {
        if (currentState == STATE_LOADING) {
            return;
        }
        hideCurrentView();
        currentState = STATE_LOADING;
        viewLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateMain() {
        if (currentState == STATE_MAIN) {
            return;
        }
        hideCurrentView();
        currentState = STATE_MAIN;
        viewMain.setVisibility(View.VISIBLE);
    }

    @Override
    public void stateError() {
        if (currentState == STATE_ERROR) {
            return;
        }
        hideCurrentView();
        currentState = STATE_ERROR;
        viewError.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishRefreshOrLoadMore() {
        if (viewMain instanceof SmartRefreshLayout) {
            ((SmartRefreshLayout) viewMain).finishRefresh(); //结束刷新
            ((SmartRefreshLayout) viewMain).finishLoadMore();//结束加载
        }
    }
}
