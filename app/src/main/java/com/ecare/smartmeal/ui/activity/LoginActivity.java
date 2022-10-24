package com.ecare.smartmeal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseActivity;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.LoginContract;
import com.ecare.smartmeal.model.bean.rsp.LoginRspDTO;
import com.ecare.smartmeal.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_show_hide_password)
    ImageView ivShowHidePassword;
    //是否显示密码，默认false
    private boolean mIsShowPassword;

    @Override
    protected LoginContract.Presenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_show_hide_password, R.id.tv_forgot_password, R.id.tv_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show_hide_password:
                if (mIsShowPassword) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowHidePassword.setImageResource(R.drawable.icon_login_show_password);
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowHidePassword.setImageResource(R.drawable.icon_login_hide_password);
                }
                mIsShowPassword = !mIsShowPassword;
                break;
            case R.id.tv_forgot_password:
                RegisterOrRetrievePasswordActivity.start(mContext, RegisterOrRetrievePasswordActivity.TYPE_RETRIEVE_PASSWORD);
                break;
            case R.id.tv_login:
                String mobile = etMobile.getText().toString();
                if (!RegexUtils.isMobileSimple(mobile)) {
                    showMsg("请输入正确的手机号");
                    break;
                }
                String password = etPassword.getText().toString();
                if (StringUtils.isEmpty(password)) {
                    showMsg("请输入密码");
                    break;
                }
                mPresenter.login(mobile, password);
                break;
            case R.id.tv_register:
                RegisterOrRetrievePasswordActivity.start(mContext, RegisterOrRetrievePasswordActivity.TYPE_REGISTER);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void loginSuc(LoginRspDTO data) {
        if (data == null) {
            showMsg("登录失败，请重试");
            return;
        }
        SPUtils.getInstance(Constants.SP_USER).put(Constants.SP_TOKEN, data.getToken());
        SPUtils.getInstance(Constants.SP_USER).put(Constants.SP_MOBILE, data.getMobile());
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
