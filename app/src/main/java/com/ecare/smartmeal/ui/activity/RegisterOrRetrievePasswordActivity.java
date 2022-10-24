package com.ecare.smartmeal.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseActivity;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.RegisterOrRetrievePasswordContract;
import com.ecare.smartmeal.presenter.RegisterOrRetrievePasswordPresenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class RegisterOrRetrievePasswordActivity extends BaseActivity<RegisterOrRetrievePasswordContract.Presenter> implements RegisterOrRetrievePasswordContract.View {

    public static final int TYPE_REGISTER = 1;          //注册
    public static final int TYPE_RETRIEVE_PASSWORD = 2; //找回密码

    @IntDef({TYPE_REGISTER, TYPE_RETRIEVE_PASSWORD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_show_hide_password)
    ImageView ivShowHidePassword;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    //类型
    private int mType;
    //倒计时
    private CountDownTimer mCountDownTimer;
    //是否显示密码，默认false
    private boolean mIsShowPassword;

    /**
     * RegisterOrRetrievePasswordActivity
     *
     * @param context
     * @param type
     */
    public static void start(Context context, @Type int type) {
        Intent intent = new Intent(context, RegisterOrRetrievePasswordActivity.class);
        intent.putExtra(Constants.IT_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected RegisterOrRetrievePasswordContract.Presenter createPresenter() {
        return new RegisterOrRetrievePasswordPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_register_or_retrieve_password;
    }

    @Override
    protected void initVariables() {
        mType = getIntent().getIntExtra(Constants.IT_TYPE, TYPE_REGISTER);
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        tvTitle.setText(mType == TYPE_REGISTER ? "注册" : "找回密码");
        etPassword.setHint(mType == TYPE_REGISTER ? "请输入密码" : "请输入新密码");
        tvConfirm.setText(mType == TYPE_REGISTER ? "注册" : "确定");
    }

    @OnClick({R.id.tv_get_verification_code, R.id.iv_show_hide_password, R.id.tv_confirm})
    public void onClick(View view) {
        String mobile = null;
        switch (view.getId()) {
            case R.id.tv_get_verification_code:
                mobile = etMobile.getText().toString();
                if (!RegexUtils.isMobileSimple(mobile)) {
                    showMsg("请输入正确的手机号");
                    break;
                }
                mPresenter.getSmsCode(mobile, mType);
                break;
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
            case R.id.tv_confirm:
                mobile = etMobile.getText().toString();
                if (!RegexUtils.isMobileSimple(mobile)) {
                    showMsg("请输入正确的手机号");
                    break;
                }
                String smsCode = etVerificationCode.getText().toString();
                if (StringUtils.isEmpty(smsCode)) {
                    showMsg("请输入验证码");
                    break;
                }
                String password = etPassword.getText().toString();
                if (StringUtils.isEmpty(password)) {
                    showMsg(mType == TYPE_REGISTER ? "请输入密码" : "请输入新密码");
                    break;
                }
                if (mType == TYPE_REGISTER) {
                    mPresenter.register(mobile, password, smsCode);
                } else {
                    mPresenter.retrievePassword(mobile, password, smsCode);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void getSmsCodeSuc() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetVerificationCode.setText(millisUntilFinished / 1000 + "s");
                tvGetVerificationCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tvGetVerificationCode.setText("获取验证码");
                tvGetVerificationCode.setEnabled(true);
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void registerSuc() {
        showMsg("注册成功");
        finish();
    }

    @Override
    public void retrievePasswordSuc() {
        showMsg("找回密码成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroy();
    }
}
