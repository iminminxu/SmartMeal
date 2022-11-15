package com.ecare.smartmeal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.iot.bpaas.api.service.BPaaSCallback;
import com.alipay.iot.bpaas.api.service.BPaaSResponse;
import com.alipay.iot.bpaas.api.service.LocalService;
import com.alipay.iot.sdk.APIManager;
import com.alipay.iot.sdk.payment.PaymentAPI;
import com.blankj.utilcode.util.StringUtils;
import com.daasuu.bl.BubbleLayout;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootActivity;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.MemberRechargeContract;
import com.ecare.smartmeal.facepay.FacePayUtils;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;
import com.ecare.smartmeal.presenter.MemberRechargePresenter;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.sunmi.externalprinterlibrary.api.Status;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/23.
 * Email: iminminxu@gmail.com
 */
public class MemberRechargeActivity extends RootActivity<MemberRechargeContract.Presenter> implements MemberRechargeContract.View {

    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //会员信息
    @BindView(R.id.view_main)
    ConstraintLayout clMemberInfo;
    @BindView(R.id.tv_id_card)
    TextView tvIDCard;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_card_no)
    TextView tvCardNo;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.et_payment_amount)
    EditText etPaymentAmount;
    @BindView(R.id.et_recharge_amount)
    EditText etRechargeAmount;
    @BindView(R.id.rg_payment_method)
    RadioGroup rgPaymentMethod;
    //线下支付
    @BindView(R.id.bl_offline)
    BubbleLayout blOffline;
    @BindView(R.id.tv_offline_cash)
    TextView tvOfflineCash;
    @BindView(R.id.tv_offline_alipay)
    TextView tvOfflineAlipay;
    @BindView(R.id.tv_offline_wechat)
    TextView tvOfflineWechat;
    @BindView(R.id.tv_offline_citizen_card)
    TextView tvOfflineCitizenCard;
    @BindView(R.id.tv_offline_other)
    TextView tvOfflineOther;
    //支付结果页面
    @BindView(R.id.ll_paying)
    LinearLayout llPaying;
    @BindView(R.id.ll_pay_fail)
    LinearLayout llPayFail;
    @BindView(R.id.ll_pay_success)
    LinearLayout llPaySuccess;
    @BindView(R.id.tv_print_hint)
    TextView tvPrintHint;
    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    //支付状态
    public static final int PAY_CANCEL = 0;
    public static final int PAY_PAYING = 1;
    public static final int PAY_FAIL = 2;
    public static final int PAY_SUCCESS = 3;
    private int mPayStatus = -1;
    //支付方式
    public static final int PAYMENT_METHOD_FACE = 3;
    public static final int PAYMENT_METHOD_CASH = 5;
    public static final int PAYMENT_METHOD_ALIPAY = 6;
    public static final int PAYMENT_METHOD_WECHAT = 7;
    public static final int PAYMENT_METHOD_CITIZEN_CARD = 8;
    public static final int PAYMENT_METHOD_OTHER = 9;
    private int mPaymentMethod;
    //会员id
    private int mPosition;
    private int mCustomerId;
    private String mCardNo;
    //关闭人脸付所需数据
    private String mFtoken;
    private String mSessionId;
    private String mTradeNo;
    //倒计时
    private CountDownTimer mCountDownTimer;
    //会员信息
    private CustomerListResponse mMemberInfo;

    @Override
    protected MemberRechargeContract.Presenter createPresenter() {
        return new MemberRechargePresenter();
    }

    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mPosition = intent.getIntExtra(Constants.IT_POSITION, -1);
        mCustomerId = intent.getIntExtra(Constants.IT_CUSTOMER_ID, 0);
        mCardNo = intent.getStringExtra(Constants.IT_CARD_NO);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_member_recharge;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //设置标题
        togglePayStatus(PAY_CANCEL);
        //设置金额输入框(小数点只能输入两位;第一位为.时加0;)
        etPaymentAmount.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
        etRechargeAmount.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
        //设置线下支付
        rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                blOffline.setVisibility(checkedId == R.id.rb_offline ? View.VISIBLE : View.GONE);
            }
        });
        blOffline.setVisibility(View.GONE);
        tvOfflineCash.setSelected(true);
    }

    /**
     * 切换支付状态
     *
     * @param payStatus 支付状态
     */
    private void togglePayStatus(int payStatus) {
        if (mPayStatus == PAY_CANCEL && payStatus != PAY_PAYING) {
            //订单页只支持切换为支付中(防止取消人脸付造成状态从订单页切换为支付失败页)
            return;
        }
        mPayStatus = payStatus;
        tvTitle.setText(mPayStatus == PAY_CANCEL ? "充值" : "支付");
        switch (mPayStatus) {
            case PAY_CANCEL:
                clMemberInfo.setVisibility(View.VISIBLE);
                llPaying.setVisibility(View.GONE);
                llPayFail.setVisibility(View.GONE);
                llPaySuccess.setVisibility(View.GONE);
                break;
            case PAY_PAYING:
                clMemberInfo.setVisibility(View.GONE);
                llPaying.setVisibility(View.VISIBLE);
                llPayFail.setVisibility(View.GONE);
                llPaySuccess.setVisibility(View.GONE);
                break;
            case PAY_FAIL:
                clMemberInfo.setVisibility(View.GONE);
                llPaying.setVisibility(View.GONE);
                llPayFail.setVisibility(View.VISIBLE);
                llPaySuccess.setVisibility(View.GONE);
                break;
            case PAY_SUCCESS:
                clMemberInfo.setVisibility(View.GONE);
                llPaying.setVisibility(View.GONE);
                llPayFail.setVisibility(View.GONE);
                llPaySuccess.setVisibility(View.VISIBLE);
                mCountDownTimer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCountdown.setText(millisUntilFinished / 1000 + "s");
                    }

                    @Override
                    public void onFinish() {
                        if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                            FacePayUtils.closePayResult(mSessionId, null);
                        }
                        finish();
                    }
                };
                mCountDownTimer.start();
                Print();
                mMemberInfo.setBalance(mMemberInfo.getBalance().add(new BigDecimal(String.valueOf(NumUtils.parseDouble(etRechargeAmount.getText().toString())))));
                break;
            default:
                break;
        }
    }

    /**
     * 普通打印一单小票
     * 普通打印不关心打印执行结果
     * 建议打印接口调用在非主线程（网络打印机必须在非主线程）
     */
    public void Print() {
        try {
            int status = SunmiPrinterApi.getInstance().getPrinterStatus();
            if (!SunmiPrinterApi.getInstance().isConnected() || status != Status.RUNNING) {
                tvPrintHint.setText("未连接打印机，请确认打印机是否连接！");
                return;
            }
            tvPrintHint.setText("请取走小票凭证！");
            PrintUtils.printMemberRechargeOrder(mMemberInfo, new BigDecimal(String.valueOf(NumUtils.parseDouble(etRechargeAmount.getText().toString()))), new BigDecimal(String.valueOf(NumUtils.parseDouble(etPaymentAmount.getText().toString()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.dtv_back, R.id.tv_offline_cash, R.id.tv_offline_alipay, R.id.tv_offline_wechat, R.id.tv_offline_citizen_card, R.id.tv_offline_other, R.id.tv_recharge, R.id.tv_pay_again, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_back:
            case R.id.tv_back:
                backClick();
                break;
            case R.id.tv_offline_cash:
            case R.id.tv_offline_alipay:
            case R.id.tv_offline_wechat:
            case R.id.tv_offline_citizen_card:
            case R.id.tv_offline_other:
                toggleOffline(view);
                break;
            case R.id.tv_recharge:
                if (mMemberInfo == null) {
                    showMsg("会员信息加载失败");
                    break;
                }
                String paymentAmountStr = etPaymentAmount.getText().toString();
                if (StringUtils.isEmpty(paymentAmountStr)) {
                    showMsg("请输入实付金额");
                    break;
                }
                String rechargeAmountStr = etRechargeAmount.getText().toString();
                if (StringUtils.isEmpty(rechargeAmountStr)) {
                    showMsg("请输入充值金额");
                    break;
                }
                if (NumUtils.parseDouble(rechargeAmountStr) <= 0) {
                    showMsg("充值金额必须大于0");
                    break;
                }
                if (NumUtils.parseDouble(paymentAmountStr) > 0) {
                    if (rgPaymentMethod.getCheckedRadioButtonId() == R.id.rb_online) {
                        mPaymentMethod = PAYMENT_METHOD_FACE;
                    } else {
                        if (tvOfflineCash.isSelected()) {
                            mPaymentMethod = PAYMENT_METHOD_CASH;
                        } else if (tvOfflineAlipay.isSelected()) {
                            mPaymentMethod = PAYMENT_METHOD_ALIPAY;
                        } else if (tvOfflineWechat.isSelected()) {
                            mPaymentMethod = PAYMENT_METHOD_WECHAT;
                        } else if (tvOfflineCitizenCard.isSelected()) {
                            mPaymentMethod = PAYMENT_METHOD_CITIZEN_CARD;
                        } else if (tvOfflineOther.isSelected()) {
                            mPaymentMethod = PAYMENT_METHOD_OTHER;
                        }
                    }
                } else {
                    mPaymentMethod = PAYMENT_METHOD_CASH;
                }
                recharge();
                break;
            case R.id.tv_pay_again:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closePayResult(mSessionId, null);
                }
                recharge();
                break;
            default:
                break;
        }
    }

    /**
     * 返回
     */
    private void backClick() {
        switch (mPayStatus) {
            case PAY_PAYING:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closeScanFace(mSessionId, null);
                    togglePayStatus(PAY_CANCEL);
                } else {
                    showMsg("正在支付中，请勿退出");
                }
                break;
            case PAY_FAIL:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closePayResult(mSessionId, null);
                }
                togglePayStatus(PAY_CANCEL);
                break;
            case PAY_SUCCESS:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closePayResult(mSessionId, null);
                }
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void toggleOffline(View view) {
        tvOfflineCash.setSelected(view == tvOfflineCash);
        tvOfflineAlipay.setSelected(view == tvOfflineAlipay);
        tvOfflineWechat.setSelected(view == tvOfflineWechat);
        tvOfflineCitizenCard.setSelected(view == tvOfflineCitizenCard);
        tvOfflineOther.setSelected(view == tvOfflineOther);
    }

    private void recharge() {
        togglePayStatus(PAY_PAYING);
        if (mPaymentMethod == PAYMENT_METHOD_FACE) {
            FacePayUtils.startScanFace(new BigDecimal(String.valueOf(NumUtils.parseDouble(etPaymentAmount.getText().toString()))), new BPaaSCallback() {
                @Override
                public void onResponse(BPaaSResponse response) {
                    if (response == null || response.getCode() == BPaaSResponse.RESULT_CODE_FAIL || response.getResult() == null) {
                        return;
                    }
                    Bundle result = response.getResult().getBundle(LocalService.KEY_LOCAL_RESULT);
                    if (result == null) {
                        return;
                    }
                    // 处理刷脸结果
                    int code = result.getInt("code");
                    if (code == BPaaSResponse.RESULT_CODE_SUCCESS) {
                        Bundle extInfo = result.getBundle("extInfo");
                        if (extInfo != null) {
                            String resultStr = extInfo.getString("result");
                            if (!StringUtils.isEmpty(resultStr)) {
                                JSONObject extJson = JSONObject.parseObject(resultStr);
                                mFtoken = extJson.getString("ftoken");
                                if (StringUtils.isEmpty(mFtoken)) {
                                    togglePayStatus(PAY_FAIL);
                                } else {
                                    //成功
                                    MerchantInfoResponse merchantInfo = App.getInstance().getMerchantInfo();
                                    PaymentAPI api = APIManager.getInstance().getPaymentAPI();
                                    String terminalParams = api.signWithFaceToken(mFtoken, String.valueOf(NumUtils.parseDouble(etPaymentAmount.getText().toString())));
                                    mPresenter.pay("收银台支付", String.valueOf(NumUtils.parseDouble(etPaymentAmount.getText().toString())), mFtoken, mTradeNo, merchantInfo == null ? "" : merchantInfo.getMerchantId(), extJson.getString("alipayUid"), terminalParams);
                                }
                            }
                        }
                    } else {
                        togglePayStatus(PAY_FAIL);
                    }
                }

                @Override
                public void onEvent(String s, String s1, Bundle bundle) {
                    JSONObject response = JSON.parseObject(s1);
                    String eventType = response.getString("messageType");
                    JSONObject content = JSON.parseObject(response.getString("messageContent"));
                    if (eventType.equals("SMILE_NEW_SESSION")) {
                        // 每次刷脸都会将当次的sessionId和tradeNo通过onEvent回调过来
                        mSessionId = content.getString("sessionId");
                        mTradeNo = content.getString("tradeNo");
                    }
                }
            });
        } else {
            mPresenter.memberRecharge(mCustomerId, mPaymentMethod, new BigDecimal(String.valueOf(NumUtils.parseDouble(etPaymentAmount.getText().toString()))), new BigDecimal(String.valueOf(NumUtils.parseDouble(etRechargeAmount.getText().toString()))), mCardNo);
        }
    }

    @Override
    protected void doBusiness() {
        stateLoading();
        mPresenter.getMemberInfo(mCustomerId, mCardNo);
    }

    @Override
    protected void firstLoadDataFailRetry() {
        stateLoading();
        mPresenter.getMemberInfo(mCustomerId, mCardNo);
    }

    @Override
    public void setMemberInfo(CustomerListResponse data) {
        if (data == null) {
            return;
        }
        mMemberInfo = data;
        tvIDCard.setText(data.getIdCard());
        tvName.setText(data.getName());
        tvMobile.setText(data.getMobile());
        tvCardNo.setText(data.getCardNo());
        tvBalance.setText(NumUtils.parsePrintAmount(data.getBalance()));
    }

    @Override
    public void paySuc(boolean data) {
        BigDecimal paymentAmount = new BigDecimal(String.valueOf(NumUtils.parseDouble(etPaymentAmount.getText().toString())));
        FacePayUtils.goOfflineFacePayResult(data, mFtoken, mSessionId, mTradeNo, new BigDecimal(0), paymentAmount, paymentAmount, null);
        if (data) {
            togglePayStatus(PAY_SUCCESS);
            mPresenter.memberRecharge(mCustomerId, mPaymentMethod, paymentAmount, new BigDecimal(String.valueOf(NumUtils.parseDouble(etRechargeAmount.getText().toString()))), mCardNo);
        } else {
            togglePayStatus(PAY_FAIL);
        }
    }

    @Override
    public void memberRechargeSuc(boolean data) {
        if (mPaymentMethod == PAYMENT_METHOD_FACE) {
            return;
        }
        togglePayStatus(data ? PAY_SUCCESS : PAY_FAIL);
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    @Override
    public void finish() {
        if (mMemberInfo != null) {
            Intent intent = new Intent();
            intent.putExtra(Constants.IT_POSITION, mPosition);
            intent.putExtra(Constants.IT_CUSTOMER_ID, mCustomerId);
            intent.putExtra(Constants.IT_CARD_NO, mCardNo);
            intent.putExtra(Constants.IT_BALANCE, mMemberInfo.getBalance());
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }
}
