package com.ecare.smartmeal.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.ui.activity.LoginActivity;
import com.ecare.smartmeal.ui.activity.RegisterOrRetrievePasswordActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/7.
 * Email: iminminxu@gmail.com
 */
public class LogoutPopup extends AttachPopupView {

    private Context mContext;

    public LogoutPopup(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_logout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tv_logout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(mContext).asConfirm("提示", "确定退出登录？", "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        SPUtils.getInstance(Constants.SP_USER).clear();
                        ActivityUtils.finishAllActivities(true);
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    }
                }, null, false).show();
                dismiss();
            }
        });
        findViewById(R.id.tv_forgot_password).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterOrRetrievePasswordActivity.start(mContext, RegisterOrRetrievePasswordActivity.TYPE_RETRIEVE_PASSWORD);
                dismiss();
            }
        });
    }
}
