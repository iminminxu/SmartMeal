package com.ecare.smartmeal.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alipay.iot.sdk.APIManager;
import com.alipay.iot.sdk.InitFinishCallback;
import com.alipay.zoloz.smile2pay.MetaInfoCallback;
import com.alipay.zoloz.smile2pay.Zoloz;
import com.alipay.zoloz.smile2pay.ZolozConnectCallback;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseActivity;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.MainContract;
import com.ecare.smartmeal.facepay.FacePayUtils;
import com.ecare.smartmeal.facepay.FaceRecognitionUtils;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;
import com.ecare.smartmeal.presenter.MainPresenter;
import com.ecare.smartmeal.ui.fragment.HomeCashierFragment;
import com.ecare.smartmeal.ui.fragment.HomePendingFragment;
import com.ecare.smartmeal.ui.fragment.HomeRechargeFragment;
import com.ecare.smartmeal.widget.LogoutPopup;
import com.lxj.xpopup.XPopup;
import com.sunmi.externalprinterlibrary.api.ConnectCallback;
import com.sunmi.externalprinterlibrary.api.PrinterException;
import com.sunmi.externalprinterlibrary.api.SunmiPrinter;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/13.
 * Email: iminminxu@gmail.com
 */
public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    //TAG
    public static final String TAG_CASHIER = "cashier";
    public static final String TAG_PENDING = "pending";
    public static final String TAG_RECHARGE = "recharge";
    //控件
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.ll_cashier)
    LinearLayout llCashier;
    @BindView(R.id.ll_pending)
    LinearLayout llPending;
    @BindView(R.id.ll_recharge)
    LinearLayout llRecharge;
    //当前Fragment
    private Fragment mCurrentFragment;
    //定时24小时获取authToken
    private Disposable mSubscribe;
    //蓝牙连接打印机
    private Disposable mBleSubscribe;

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        tvMobile.setText(desensitizedPhoneNumber(SPUtils.getInstance(Constants.SP_USER).getString(Constants.SP_MOBILE)));
        switchFragment(TAG_CASHIER);
        //保存银联配置文件
        saveUnionPayConfigurationFile();
        //初始化IoT
        initIoT();
        //连接打印机
        connectPrinter();
    }

    /**
     * 手机号脱敏
     *
     * @param phoneNumber 手机号
     * @return 脱敏后手机号
     */
    public static String desensitizedPhoneNumber(String phoneNumber) {
        if (!StringUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }

    /**
     * 切换Fragment
     *
     * @param tag TAG
     */
    private void switchFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //隐藏当前fragment
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        mCurrentFragment = fm.findFragmentByTag(tag);
        if (mCurrentFragment == null) {
            mCurrentFragment = getFragment(tag);
            transaction.add(R.id.fl_content, mCurrentFragment, tag);
        } else {
            transaction.show(mCurrentFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 获取Fragment
     *
     * @param tag TAG
     * @return Fragment
     */
    private Fragment getFragment(String tag) {
        switch (tag) {
            case TAG_PENDING:
                return new HomePendingFragment();
            case TAG_RECHARGE:
                return new HomeRechargeFragment();
            default:
                return new HomeCashierFragment();
        }
    }

    /**
     * 保存银联配置文件
     */
    private void saveUnionPayConfigurationFile() {
        Disposable disposable = new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        File file = new File(SDCardUtils.getSDCardPathByEnvironment() + File.separator + "Android" + File.separator + "SP30POS.INI");
                        if (!file.exists()) {
                            ResourceUtils.copyFileFromAssets("SP30POS.INI", file.getAbsolutePath());
                        }
                    }
                });
    }

    /**
     * 初始化IoT
     */
    private void initIoT() {
        if (!StringUtils.isEmpty(APIManager.getInstance().getDeviceAPI().getDeviceSn())) {
            //获取商家信息
            mPresenter.getMerchantInfo();
            return;
        }
        try {
            //iot sdk初始化成功后，再初始化SDK
            APIManager.getInstance().initialize(getApplicationContext(), Constants.KEY_ABCP_APPLICATION_APPID, new InitFinishCallback() {
                @Override
                public void initFinished(boolean success) {
                    if (success) {
                        //获取商家信息
                        mPresenter.getMerchantInfo();
                    } else {
                        showMsg("IoT初始化失败");
                    }
                }
            });
        } catch (Exception e) {
            showMsg("IoT初始化失败");
        }
    }

    /**
     * 连接打印机
     */
    private void connectPrinter() {
        mBleSubscribe = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    SunmiPrinterApi.getInstance().connectPrinter(mContext, SunmiPrinter.SunmiBlueToothPrinter, new ConnectCallback() {
                        @Override
                        public void onFound() {

                        }

                        @Override
                        public void onUnfound() {

                        }

                        @Override
                        public void onConnect() {

                        }

                        @Override
                        public void onDisconnect() {

                        }
                    });
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
    }

    @OnClick({R.id.iv_avatar, R.id.tv_statistical_statement, R.id.ll_cashier, R.id.ll_pending, R.id.ll_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                new XPopup.Builder(mContext)
                        .isDestroyOnDismiss(true)
                        .atView(view)
                        .isViewMode(true)
                        .hasShadowBg(true)
                        .isCenterHorizontal(true)
                        .offsetY((int) getResources().getDimension(R.dimen.dp_27))
                        .asCustom(new LogoutPopup(mContext))
                        .show();
                break;
            case R.id.tv_statistical_statement:
                Intent intent = new Intent(mContext, WebPageActivity.class);
                intent.putExtra(Constants.IT_TITLE, "统计报表");
                intent.putExtra(Constants.IT_URL, Constants.URL_STATISTICAL_STATEMENT + SPUtils.getInstance(Constants.SP_USER).getString(Constants.SP_MOBILE));
                startActivity(intent);
                break;
            case R.id.ll_cashier:
                if (mCurrentFragment instanceof HomeCashierFragment) {
                    break;
                }
                llCashier.setBackgroundColor(getResources().getColor(R.color.color_ec7220));
                llPending.setBackgroundColor(Color.TRANSPARENT);
                llRecharge.setBackgroundColor(Color.TRANSPARENT);
                switchFragment(TAG_CASHIER);
                break;
            case R.id.ll_pending:
                if (mCurrentFragment instanceof HomePendingFragment) {
                    break;
                }
                llCashier.setBackgroundColor(Color.TRANSPARENT);
                llPending.setBackgroundColor(getResources().getColor(R.color.color_ec7220));
                llRecharge.setBackgroundColor(Color.TRANSPARENT);
                switchFragment(TAG_PENDING);
                break;
            case R.id.ll_recharge:
                if (mCurrentFragment instanceof HomeRechargeFragment) {
                    break;
                }
                llCashier.setBackgroundColor(Color.TRANSPARENT);
                llPending.setBackgroundColor(Color.TRANSPARENT);
                llRecharge.setBackgroundColor(getResources().getColor(R.color.color_ec7220));
                switchFragment(TAG_RECHARGE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void setMerchantInfo(MerchantInfoResponse data) {
        if (data == null) {
            showMsg("获取商家信息失败，请退出重试");
            return;
        }
        //设置商家信息，待处理使用
        App.getInstance().setMerchantInfo(data);
        //初始化人脸付
        FacePayUtils.init(data.getMerchantId(), data.getMerchantName());
        //初始化人脸识别，定时24小时(authToken24小时失效)
        mSubscribe = Flowable.interval(0, 24, TimeUnit.HOURS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        FaceRecognitionUtils.getMetaInfo(data.getMerchantId(), data.getMerchantName(), new MetaInfoCallback() {
                            @Override
                            public void onMetaInfo(String metaInfo, Map<String, Object> map) {
                                if (StringUtils.isEmpty(metaInfo)) {
                                    showMsg("人脸识别初始化失败");
                                    return;
                                }
                                mPresenter.getAuthToken(metaInfo);
                            }
                        });
                    }
                });

    }

    @Override
    public void setAuthToken(String authToken) {
        //初始化人脸识别
        FaceRecognitionUtils.init(App.getInstance().getMerchantInfo().getMerchantId(), App.getInstance().getMerchantInfo().getMerchantName(), authToken);
        Zoloz.getInstance(App.getInstance()).setConnectCallback(new ZolozConnectCallback() {
            @Override
            public void onConnect(boolean connected, ComponentName componentName) {
                if (!connected) {
                    FaceRecognitionUtils.init(App.getInstance().getMerchantInfo().getMerchantId(), App.getInstance().getMerchantInfo().getMerchantName(), authToken);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mBleSubscribe != null) {
            mBleSubscribe.dispose();
        }
        if (mSubscribe != null) {
            mSubscribe.dispose();
        }
        try {
            SunmiPrinterApi.getInstance().disconnectPrinter(mContext);
        } catch (PrinterException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
