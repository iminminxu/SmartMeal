package com.ecare.smartmeal.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.SimpleActivity;
import com.ecare.smartmeal.config.Constants;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class SplashActivity extends SimpleActivity implements CancelAdapt {

    @Override
    protected int getLayoutId() {
        return R.layout.act_splash;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        String token = SPUtils.getInstance(Constants.SP_USER).getString(Constants.SP_TOKEN);
        if (StringUtils.isEmpty(token)) {
            startActivity(new Intent(mContext, LoginActivity.class));
        } else {
            startActivity(new Intent(mContext, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void doBusiness() {

    }
}
