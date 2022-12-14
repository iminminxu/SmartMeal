package com.ecare.smartmeal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.iot.bpaas.api.service.BPaaSCallback;
import com.alipay.iot.bpaas.api.service.BPaaSResponse;
import com.alipay.iot.bpaas.api.service.LocalService;
import com.alipay.iot.sdk.APIManager;
import com.alipay.iot.sdk.payment.PaymentAPI;
import com.alipay.zoloz.smile2pay.Zoloz;
import com.alipay.zoloz.smile2pay.ZolozConfig;
import com.alipay.zoloz.smile2pay.verify.Smile2PayResponse;
import com.alipay.zoloz.smile2pay.verify.VerifyCallback;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.daasuu.bl.BubbleLayout;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootActivity;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.ConfirmOrderContract;
import com.ecare.smartmeal.facepay.FacePayUtils;
import com.ecare.smartmeal.facepay.FaceRecognitionUtils;
import com.ecare.smartmeal.model.bean.req.CommodityOrderRequest;
import com.ecare.smartmeal.model.bean.req.ElderAuthRequest;
import com.ecare.smartmeal.model.bean.rsp.CommodityItem;
import com.ecare.smartmeal.model.bean.rsp.CommodityOrderPayResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;
import com.ecare.smartmeal.model.bean.rsp.ElderCodeResponse;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderInfoResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.presenter.ConfirmOrderPresenter;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.ecare.smartmeal.utils.TextSpanBuilder;
import com.ecare.smartmeal.widget.DiscountPopup;
import com.ecare.smartmeal.widget.HorizontalDividerItemDecoration;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.sunmi.externalprinterlibrary.api.Status;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/26.
 * Email: iminminxu@gmail.com
 */
public class ConfirmOrderActivity extends RootActivity<ConfirmOrderContract.Presenter> implements ConfirmOrderContract.View {

    //??????
    @BindView(R.id.tb_title)
    Toolbar tbTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_line)
    View viewLine;
    //????????????
    @BindView(R.id.view_main)
    LinearLayout llConfirmOrder;
    //??????????????????
    @BindView(R.id.rv_selected_dishes)
    RecyclerView rvSelectedDishes;
    private BaseQuickAdapter<CommodityxAllResponseItem, BaseViewHolder> mSelectedDishesAdapter;
    //???????????????????????????
    @BindView(R.id.tv_dine)
    TextView tvDine;
    @BindView(R.id.tv_pack)
    TextView tvPack;
    @BindView(R.id.tv_get_identity)
    TextView tvGetIdentity;
    @BindView(R.id.tv_mobile_identify)
    TextView tvMobileIdentify;
    @BindView(R.id.tv_id_card_input)
    TextView tvIDCardInput;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_member_info)
    TextView tvMemberInfo;
    @BindView(R.id.tv_member_taboo)
    TextView tvMemberTaboo;
    //???????????????
    @BindView(R.id.tv_packing_fee)
    TextView tvPackingFee;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_total_receivable)
    TextView tvTotalReceivable;
    @BindView(R.id.et_total_paid_in)
    EditText etTotalPaidIn;
    @BindView(R.id.rg_payment_method)
    RadioGroup rgPaymentMethod;
    //????????????
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
    //??????????????????
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
    //????????????
    public static final int PAY_CANCEL = 0;
    public static final int PAY_PAYING = 1;
    public static final int PAY_FAIL = 2;
    public static final int PAY_SUCCESS = 3;
    private int mPayStatus = -1;
    //????????????
    public static final int PAYMENT_METHOD_BALANCE = 1;
    public static final int PAYMENT_METHOD_FACE = 3;
    public static final int PAYMENT_METHOD_CASH = 5;
    public static final int PAYMENT_METHOD_ALIPAY = 6;
    public static final int PAYMENT_METHOD_WECHAT = 7;
    public static final int PAYMENT_METHOD_CITIZEN_CARD = 8;
    public static final int PAYMENT_METHOD_OTHER = 9;
    private int mPaymentMethod;
    //??????????????????
    private ArrayList<CommodityxAllResponseItem> mSelectedDishes;
    //????????????
    private int mEatWay;
    //????????????
    private ElderCodeResponse mElderInfo;
    //????????????
    private String mTaboos;
    //???????????????
    private OrderInfoResponse mPreOrderInfo;
    //??????id
    private int mOrderId;
    //???????????????????????????
    private String mFtoken;
    private String mSessionId;
    private String mTradeNo;
    //???????????????????????????????????????????????????
    private CommodityOrderPayResponse mCommodityOrderPayResponse;
    //?????????
    private CountDownTimer mCountDownTimer;

    @Override
    protected ConfirmOrderContract.Presenter createPresenter() {
        return new ConfirmOrderPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_confirm_order;
    }

    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mSelectedDishes = intent.getParcelableArrayListExtra(Constants.IT_SELECTED_DISHES);
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //??????????????????
        View loading = findViewById(R.id.view_loading);
        loading.setBackgroundResource(R.drawable.corners_3_solid_white);
        LinearLayout.LayoutParams loadingParams = (LinearLayout.LayoutParams) loading.getLayoutParams();
        loadingParams.setMargins(0, (int) getResources().getDimension(R.dimen.dp_13), 0, 0);
        loading.setLayoutParams(loadingParams);
        View error = findViewById(R.id.view_error);
        error.setBackgroundResource(R.drawable.corners_3_solid_white);
        LinearLayout.LayoutParams errorParams = (LinearLayout.LayoutParams) error.getLayoutParams();
        errorParams.setMargins(0, (int) getResources().getDimension(R.dimen.dp_13), 0, 0);
        error.setLayoutParams(errorParams);
        //????????????
        togglePayStatus(PAY_CANCEL);
        //????????????????????????
        setSelectedDishesRecyclerView();
        //????????????????????????
        setEatWay(1);
        //???????????????????????????(???????????????????????????;????????????.??????0;)
        etTotalPaidIn.setFilters(new InputFilter[]{new InputFilter() {
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
        etTotalPaidIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //????????????C???
                if (mPreOrderInfo == null) {
                    return;
                }
                String actualAmount = String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString()));
                FacePayUtils.showOrHideSKU(mSelectedDishes, mPreOrderInfo.getTicketDiscountMoney(), mPreOrderInfo.getTotalFee(), new BigDecimal(actualAmount), null);
            }
        });
        //??????????????????
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
     * ??????????????????
     *
     * @param payStatus ????????????
     */
    private void togglePayStatus(int payStatus) {
        if (mPayStatus == PAY_CANCEL && payStatus != PAY_PAYING) {
            //????????????????????????????????????(?????????????????????????????????????????????????????????????????????)
            return;
        }
        mPayStatus = payStatus;
        if (mPayStatus == PAY_CANCEL) {
            tbTitle.setBackgroundResource(R.drawable.corners_3_solid_white);
            tvTitle.setText("????????????");
            viewLine.setVisibility(View.INVISIBLE);
        } else {
            tbTitle.setBackgroundResource(R.drawable.corners_3_3_0_0_solid_white);
            tvTitle.setText("??????");
            viewLine.setVisibility(View.VISIBLE);
        }
        switch (mPayStatus) {
            case PAY_CANCEL:
                llConfirmOrder.setVisibility(View.VISIBLE);
                llPaying.setVisibility(View.GONE);
                llPayFail.setVisibility(View.GONE);
                llPaySuccess.setVisibility(View.GONE);
                break;
            case PAY_PAYING:
                llConfirmOrder.setVisibility(View.GONE);
                llPaying.setVisibility(View.VISIBLE);
                llPayFail.setVisibility(View.GONE);
                llPaySuccess.setVisibility(View.GONE);
                break;
            case PAY_FAIL:
                llConfirmOrder.setVisibility(View.GONE);
                llPaying.setVisibility(View.GONE);
                llPayFail.setVisibility(View.VISIBLE);
                llPaySuccess.setVisibility(View.GONE);
                break;
            case PAY_SUCCESS:
                llConfirmOrder.setVisibility(View.GONE);
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
                        setResult(RESULT_OK);
                        finish();
                    }
                };
                mCountDownTimer.start();
                Print();
                break;
            default:
                break;
        }
    }

    private void setSelectedDishesRecyclerView() {
        rvSelectedDishes.setLayoutManager(new LinearLayoutManager(mContext));
        mSelectedDishesAdapter = new BaseQuickAdapter<CommodityxAllResponseItem, BaseViewHolder>(R.layout.item_selected_dishes_confirm, mSelectedDishes) {
            @Override
            protected void convert(BaseViewHolder holder, CommodityxAllResponseItem item) {
                if (item == null) {
                    return;
                }
                StringBuilder builder = null;
                List<CommodityItem> commodityComb = item.getCommodityComb();
                if (commodityComb != null && commodityComb.size() != 0) {
                    builder = new StringBuilder();
                    for (int i = 0; i < commodityComb.size(); i++) {
                        CommodityItem commodityItem = commodityComb.get(i);
                        if (commodityItem == null) {
                            continue;
                        }
                        builder.append(commodityItem.getName()).append(" x").append(commodityItem.getNum());
                        if (i != commodityComb.size() - 1) {
                            builder.append("\n");
                        }
                    }
                }
                holder.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_quantity, "x" + item.getNum())
                        .setText(R.id.tv_content, builder == null ? "" : builder.toString())
                        .setGone(R.id.tv_content, builder == null)
                        .setText(R.id.tv_price, NumUtils.parseAmount(item.getPrice()));
            }
        };
        rvSelectedDishes.setAdapter(mSelectedDishesAdapter);
        rvSelectedDishes.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_e6e6e6))
                .margin((int) getResources().getDimension(R.dimen.dp_13), (int) getResources().getDimension(R.dimen.dp_13))
                .sizeResId(R.dimen.dp_1)
                .showLastDivider()
                .build());
    }

    /**
     * ??????????????????
     *
     * @param eatWay 1?????? 2????????????
     */
    private void setEatWay(int eatWay) {
        mEatWay = eatWay;
        tvDine.setSelected(eatWay == 1);
        tvPack.setSelected(eatWay == 2);
    }

    @OnClick({R.id.dtv_back, R.id.tv_dine, R.id.tv_pack, R.id.tv_get_identity, R.id.tv_mobile_identify, R.id.tv_id_card_input, R.id.tv_discount, R.id.tv_offline_cash, R.id.tv_offline_alipay, R.id.tv_offline_wechat, R.id.tv_offline_citizen_card, R.id.tv_offline_other, R.id.tv_pay, R.id.tv_pay_again, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_back:
            case R.id.tv_back:
                backClick();
                break;
            case R.id.tv_dine:
            case R.id.tv_pack:
                setEatWay(view.getId() == R.id.tv_dine ? 1 : 2);
                mPresenter.getOrderInfo(0, mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes);
                break;
            case R.id.tv_get_identity:
                speedyVerify();
                break;
            case R.id.tv_mobile_identify:
                new XPopup.Builder(mContext)
                        .isDestroyOnDismiss(true)
                        .autoOpenSoftInput(true)
                        .asInputConfirm("????????????", "?????????????????????????????????", "??????????????????",
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        if (!RegexUtils.isMobileSimple(text)) {
                                            showMsg("???????????????????????????");
                                            return;
                                        }
                                        mPresenter.getElderInfo(new ElderAuthRequest("", "", "", text), "");
                                    }
                                }).show();
                break;
            case R.id.tv_id_card_input:
                new XPopup.Builder(mContext)
                        .isDestroyOnDismiss(true)
                        .autoOpenSoftInput(true)
                        .asInputConfirm("????????????", "????????????????????????", "?????????????????????",
                                new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        if (!RegexUtils.isIDCard18(text)) {
                                            showMsg("??????????????????????????????");
                                            return;
                                        }
                                        mPresenter.getElderInfo(new ElderAuthRequest("", "", text, ""), "??????");
                                    }
                                }).show();
                break;
            case R.id.tv_discount:
                if (mPreOrderInfo == null) {
                    break;
                }
                ((DiscountPopup) new XPopup.Builder(mContext)
                        .isDestroyOnDismiss(true)
                        .autoOpenSoftInput(true)
                        .asCustom(new DiscountPopup(mContext)))
                        .setOnDiscountListener(new DiscountPopup.OnDiscountListener() {
                            @Override
                            public void onDiscount(BigDecimal discount) {
                                etTotalPaidIn.setText(mPreOrderInfo.getTotalFee().multiply(discount).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP).toString());
                            }
                        })
                        .show();
                break;
            case R.id.tv_offline_cash:
            case R.id.tv_offline_alipay:
            case R.id.tv_offline_wechat:
            case R.id.tv_offline_citizen_card:
            case R.id.tv_offline_other:
                toggleOffline(view);
                break;
            case R.id.tv_pay:
                if (mPreOrderInfo == null) {
                    break;
                }
                String totalStr = etTotalPaidIn.getText().toString();
                if (StringUtils.isEmpty(totalStr)) {
                    showMsg("?????????????????????");
                    break;
                }
                if (NumUtils.parseDouble(totalStr) > mPreOrderInfo.getAmount().doubleValue()) {
                    showMsg("????????????????????????");
                    break;
                }
                if (NumUtils.parseDouble(totalStr) > 0) {
                    if (rgPaymentMethod.getCheckedRadioButtonId() == R.id.rb_online) {
                        mPaymentMethod = mElderInfo == null || mElderInfo.getCustomerId() == 0 ? PAYMENT_METHOD_FACE : PAYMENT_METHOD_BALANCE;
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
                if (StringUtils.isEmpty(mTaboos)) {
                    mPresenter.createOrder(new BigDecimal(String.valueOf(NumUtils.parseDouble(totalStr))), mPreOrderInfo.getCouponsId(), mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes, mPreOrderInfo.getTicketDiscountMoney(), mElderInfo == null ? "" : mElderInfo.getName());
                } else {
                    new XPopup.Builder(mContext).asConfirm("??????", "???????????????????????????????????????????????????????????????", "??????", "??????", new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            mPresenter.createOrder(new BigDecimal(String.valueOf(NumUtils.parseDouble(totalStr))), mPreOrderInfo.getCouponsId(), mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes, mPreOrderInfo.getTicketDiscountMoney(), mElderInfo == null ? "" : mElderInfo.getName());
                        }
                    }, null, false, R.layout.pop_taboos).show();
                }
                break;
            case R.id.tv_pay_again:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closePayResult(mSessionId, null);
                }
                createOrderSuc(mOrderId);
                break;
            default:
                break;
        }
    }

    /**
     * ??????
     */
    private void backClick() {
        switch (mPayStatus) {
            case PAY_PAYING:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closeScanFace(mSessionId, null);
                    FacePayUtils.showOrHideSKU(mSelectedDishes, mPreOrderInfo.getTicketDiscountMoney(), mPreOrderInfo.getTotalFee(), new BigDecimal(String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString()))), null);
                    togglePayStatus(PAY_CANCEL);
                } else {
                    showMsg("??????????????????????????????");
                }
                break;
            case PAY_FAIL:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closePayResult(mSessionId, null);
                    FacePayUtils.showOrHideSKU(mSelectedDishes, mPreOrderInfo.getTicketDiscountMoney(), mPreOrderInfo.getTotalFee(), new BigDecimal(String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString()))), null);
                }
                togglePayStatus(PAY_CANCEL);
                break;
            case PAY_SUCCESS:
                if (mPaymentMethod == PAYMENT_METHOD_FACE) {
                    FacePayUtils.closePayResult(mSessionId, null);
                }
                setResult(RESULT_OK);
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * ????????????
     */
    private void speedyVerify() {
        LoadingPopupView popupView = new XPopup.Builder(mContext)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onDismiss(BasePopupView popupView) {
                        super.onDismiss(popupView);
                        HashMap<String, Object> params = new HashMap<>();
                        params.put(ZolozConfig.KEY_COMMAND_CODE, ZolozConfig.CommandCode.EXIT);
                        Zoloz.getInstance(App.getInstance()).command(params);
                    }
                })
                .asLoading("????????????????????????");
        popupView.show();
        FaceRecognitionUtils.speedyVerify(new VerifyCallback() {
            @Override
            public void onResponse(Smile2PayResponse smile2PayResponse) {
                new Handler(ConfirmOrderActivity.this.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        popupView.dismiss();
                        if (smile2PayResponse == null || smile2PayResponse.getCode() == Smile2PayResponse.CODE_VERIFY_FAIL || StringUtils.isEmpty(smile2PayResponse.getAlipayUid())) {
                            showMsg("??????????????????");
                            return;
                        }
                        mPresenter.getIdentity(smile2PayResponse.getAlipayUid());
                    }
                });
            }
        });
    }

    private void toggleOffline(View view) {
        tvOfflineCash.setSelected(view == tvOfflineCash);
        tvOfflineAlipay.setSelected(view == tvOfflineAlipay);
        tvOfflineWechat.setSelected(view == tvOfflineWechat);
        tvOfflineCitizenCard.setSelected(view == tvOfflineCitizenCard);
        tvOfflineOther.setSelected(view == tvOfflineOther);
    }

    @Override
    protected void doBusiness() {
        stateLoading();
        mPresenter.getOrderInfo(CommonSubscriber.SHOW_STATE, mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes);
    }

    @Override
    protected void firstLoadDataFailRetry() {
        stateLoading();
        mPresenter.getOrderInfo(CommonSubscriber.SHOW_STATE, mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes);
    }

    @Override
    public void setOrderInfo(OrderInfoResponse data) {
        if (data == null) {
            return;
        }
        mPreOrderInfo = data;
        tvPackingFee.setText(NumUtils.parseAmount(data.getPacking()));
        tvCoupon.setText(NumUtils.parseAmount(data.getTicketDiscountMoney()));
        tvTotalReceivable.setText(NumUtils.parseAmount(data.getTotalFee()));
        etTotalPaidIn.setText(new DecimalFormat("0.00").format(data.getAmount()));
        //????????????C???
        FacePayUtils.showOrHideSKU(mSelectedDishesAdapter.getData(), data.getTicketDiscountMoney(), data.getTotalFee(), data.getAmount(), null);
    }

    @Override
    public void setElderInfo(ElderCodeResponse data) {
        if (data == null) {
            return;
        }
        ToastUtils.showLong("??????????????????????????????");
        mElderInfo = data;
        tvGetIdentity.setVisibility(View.GONE);
        tvMobileIdentify.setVisibility(View.GONE);
        tvIDCardInput.setVisibility(View.GONE);
        tvHint.setVisibility(View.GONE);
        tvMemberInfo.setText(TextSpanBuilder.create("???????????????  ")
                .append(data.getName()).foregroundColor(mContext.getResources().getColor(R.color.color_333333))
                .append("  ")
                .append(String.valueOf(data.getAge())).foregroundColor(mContext.getResources().getColor(R.color.color_333333))
                .append("???")
                .append(data.getBalance() == null ? "" : ("  ????????????" + NumUtils.parsePrintAmount(data.getBalance())))
                .build());
        if (data.getHasAuthentication() == 0) {
            mPresenter.checkTaboos(mElderInfo.getIdCard(), mSelectedDishes);
            mPresenter.getOrderInfo(0, mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes);
        } else {
            new XPopup.Builder(mContext)
                    .isDestroyOnDismiss(true)
                    .autoOpenSoftInput(true)
                    .asInputConfirm("????????????", "????????????????????????????????????????????????", "?????????????????????",
                            new OnInputConfirmListener() {
                                @Override
                                public void onConfirm(String text) {
                                    if (!RegexUtils.isIDCard18(text)) {
                                        showMsg("??????????????????????????????");
                                        return;
                                    }
                                    mPresenter.updateMemberInfo(mElderInfo.getCustomerId(), text, "", mElderInfo == null ? "" : mElderInfo.getCardNo());
                                }
                            }).show();
        }
    }

    @Override
    public void checkTaboosSuc(CommodityOrderRequest data) {
        if (data == null) {
            return;
        }
        List<CommodityxAllResponseItem> list = data.getList();
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (CommodityxAllResponseItem commodityxAllResponseItem : list) {
            if (commodityxAllResponseItem == null || !commodityxAllResponseItem.isTaboos()) {
                continue;
            }
            builder.append(commodityxAllResponseItem.getName()).append("\n");
        }
        String hint = builder.toString();
        tvMemberTaboo.setText(StringUtils.isEmpty(hint) ? "" : "???????????????\n" + hint);
        mTaboos = builder.toString();
    }

    @Override
    public void createOrderSuc(int data) {
        mOrderId = data;
        togglePayStatus(PAY_PAYING);
        if (mPaymentMethod == PAYMENT_METHOD_FACE) {
            FacePayUtils.startScanFace(new BigDecimal(String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString()))), new BPaaSCallback() {
                @Override
                public void onResponse(BPaaSResponse response) {
                    if (response == null || response.getCode() == BPaaSResponse.RESULT_CODE_FAIL || response.getResult() == null) {
                        return;
                    }
                    Bundle result = response.getResult().getBundle(LocalService.KEY_LOCAL_RESULT);
                    if (result == null) {
                        return;
                    }
                    // ??????????????????
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
                                    //??????
                                    MerchantInfoResponse merchantInfo = App.getInstance().getMerchantInfo();
                                    PaymentAPI api = APIManager.getInstance().getPaymentAPI();
                                    String terminalParams = api.signWithFaceToken(mFtoken, String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString())));
                                    mPresenter.pay("???????????????", String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString())), mFtoken, String.valueOf(data), merchantInfo == null ? "" : merchantInfo.getMerchantId(), extJson.getString("alipayUid"), terminalParams);
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
                        // ??????????????????????????????sessionId???tradeNo??????onEvent????????????
                        mSessionId = content.getString("sessionId");
                        mTradeNo = content.getString("tradeNo");
                    }
                }
            });
        } else {
            mPresenter.payOrder(mElderInfo == null ? 0 : mElderInfo.getCustomerId(), mOrderId, mPaymentMethod, mElderInfo == null ? "" : mElderInfo.getCardNo());
        }
    }

    @Override
    public void paySuc(boolean data) {
        FacePayUtils.goOfflineFacePayResult(data, mFtoken, mSessionId, mTradeNo, mPreOrderInfo.getTicketDiscountMoney(), mPreOrderInfo.getTotalFee(), new BigDecimal(String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString()))), null);
        if (data) {
            togglePayStatus(PAY_SUCCESS);
            mPresenter.payOrder(mElderInfo == null ? 0 : mElderInfo.getCustomerId(), mOrderId, mPaymentMethod, mElderInfo == null ? "" : mElderInfo.getCardNo());
        } else {
            togglePayStatus(PAY_FAIL);
        }
    }

    /**
     * ????????????????????????
     * ???????????????????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????
     */
    public void Print() {
        try {
            int status = SunmiPrinterApi.getInstance().getPrinterStatus();
            if (!SunmiPrinterApi.getInstance().isConnected() || status != Status.RUNNING) {
                tvPrintHint.setText("??????????????????????????????????????????????????????");
                return;
            }
            tvPrintHint.setText("????????????????????????");
            PrintUtils.printDineInOrder(mSelectedDishes, mPreOrderInfo.getPacking(), mPreOrderInfo.getTicketDiscountMoney(), mPreOrderInfo.getTotalFee(),
                    new BigDecimal(String.valueOf(NumUtils.parseDouble(etTotalPaidIn.getText().toString()))), mPaymentMethod == PAYMENT_METHOD_BALANCE ? mCommodityOrderPayResponse : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void payOrderSuc(CommodityOrderPayResponse data) {
        if (mPaymentMethod == PAYMENT_METHOD_FACE) {
            return;
        }
        mCommodityOrderPayResponse = data;
        togglePayStatus(PAY_SUCCESS);
    }

    @Override
    public void payOrderFail() {
        if (mPaymentMethod == PAYMENT_METHOD_FACE) {
            return;
        }
        togglePayStatus(PAY_FAIL);
    }

    @Override
    public void updateMemberInfoSuc(String idCard) {
        mElderInfo.setIdCard(idCard);
        mPresenter.checkTaboos(mElderInfo.getIdCard(), mSelectedDishes);
        mPresenter.getOrderInfo(0, mEatWay, mElderInfo == null ? "" : mElderInfo.getIdCard(), mSelectedDishes);
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

    //????????????
    private StringBuffer mBuffer = new StringBuffer();
    private boolean mCaps;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //??????????????????????????????????????????????????????????????????KeyEvent
        if (event.getDeviceId() == KeyCharacterMap.VIRTUAL_KEYBOARD || tvGetIdentity.getVisibility() == View.GONE) {
            return super.dispatchKeyEvent(event);
        }
        //????????????
        checkLetterStatus(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                mPresenter.getElderInfo(StringUtils.equals(event.getDevice().getName(), "SM SM-2D PRODUCT HID KBW") ? new ElderAuthRequest("", mBuffer.toString(), "", "") : new ElderAuthRequest(mBuffer.toString(), "", "", ""), "");
                mBuffer.setLength(0);
            } else {
                char aChar = getInputCode(event);
                if (aChar != 0) {
                    mBuffer.append(aChar);
                }
            }
        }
        return true;
    }

    //??????shift???
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //??????shift??????????????????
                mCaps = true;
            } else {
                //??????shift??????????????????
                mCaps = false;
            }
        }
    }

    //??????????????????
    private char getInputCode(KeyEvent event) {
        int keyCode = event.getKeyCode();
        char aChar;
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //??????
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //??????
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            //????????????
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }
        return aChar;
    }
}
