package com.ecare.smartmeal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseFragmentPageAdapter;
import com.ecare.smartmeal.base.RootActivity;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.MemberInfoContract;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.presenter.MemberInfoPresenter;
import com.ecare.smartmeal.ui.fragment.MemberRecordListFragment;
import com.ecare.smartmeal.utils.NumUtils;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/20.
 * Email: iminminxu@gmail.com
 */
public class MemberInfoActivity extends RootActivity<MemberInfoContract.Presenter> implements MemberInfoContract.View {

    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_operate)
    TextView tvTitleOperate;
    //会员信息
    @BindView(R.id.et_card_no)
    EditText etCardNo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.et_id_card)
    EditText etIdCard;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    //充值消费记录
    @BindView(R.id.tl_record)
    TabLayout tlRecord;
    @BindView(R.id.vp_record)
    ViewPager vpRecord;
    //会员id
    private int mPosition;
    private int mCustomerId;
    private String mCardNo;
    //会员信息
    private CustomerListResponse mData;
    //是否退卡
    private boolean mRefundCard;

    @Override
    protected MemberInfoContract.Presenter createPresenter() {
        return new MemberInfoPresenter();
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
        return R.layout.act_member_info;
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //设置标题
        tvTitle.setText("会员信息");
        tvTitleOperate.setVisibility(View.VISIBLE);
        tvTitleOperate.setText("退卡");
        //设置ViewPager
        String[] titles = {"充值记录", "消费记录"};
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MemberRecordListFragment.newInstance(mCustomerId, MemberRecordListFragment.BIZ_TYPE_RECHARGE));
        fragmentList.add(MemberRecordListFragment.newInstance(mCustomerId, MemberRecordListFragment.BIZ_TYPE_CONSUMPTION));
        vpRecord.setAdapter(new BaseFragmentPageAdapter(getSupportFragmentManager(), fragmentList, titles));
        tlRecord.setupWithViewPager(vpRecord);
    }

    @OnClick({R.id.dtv_back, R.id.tv_title_operate, R.id.tv_update_card_no, R.id.tv_update_id_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_back:
                finish();
                break;
            case R.id.tv_title_operate:
                if (mData == null) {
                    showMsg("会员信息加载失败");
                    break;
                }
                new XPopup.Builder(mContext).asConfirm("提示", "确认退卡后，该卡将注销。\n操作无法撤销，确认继续吗？", "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.refundCard(mCustomerId, mData.getCardNo());
                    }
                }, null, false).show();
                break;
            case R.id.tv_update_card_no:
                if (mData == null) {
                    showMsg("会员信息加载失败");
                    break;
                }
                String cardNo = etCardNo.getText().toString();
                if (StringUtils.isEmpty(cardNo)) {
                    showMsg("请填写正确的卡号");
                    break;
                }
                new XPopup.Builder(mContext).asConfirm("提示", "确定修改卡号？", "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.updateMemberInfo(mCustomerId, "", cardNo, mData.getCardNo());
                    }
                }, null, false).show();
                break;
            case R.id.tv_update_id_card:
                if (mData == null) {
                    showMsg("会员信息加载失败");
                    break;
                }
                String iDCard = etIdCard.getText().toString();
                if (!RegexUtils.isIDCard18(iDCard)) {
                    showMsg("请填写正确的身份证号");
                    break;
                }
                new XPopup.Builder(mContext).asConfirm("提示", "确定修改身份证号？", "取消", "确定", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.updateMemberInfo(mCustomerId, iDCard, "", mData.getCardNo());
                    }
                }, null, false).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {
        stateLoading();
        mPresenter.getMemberInfo(mCustomerId, mData == null ? mCardNo : mData.getCardNo());
    }

    @Override
    protected void firstLoadDataFailRetry() {
        stateLoading();
        mPresenter.getMemberInfo(mCustomerId, mData == null ? mCardNo : mData.getCardNo());
    }

    @Override
    public void setMemberInfo(CustomerListResponse data) {
        if (data == null) {
            return;
        }
        mData = data;
        etCardNo.setText(data.getCardNo());
        tvName.setText(data.getName());
        tvMobile.setText(data.getMobile());
        etIdCard.setText(data.getIdCard());
        tvAddress.setText(data.getLiveAddrx());
        tvBalance.setText(NumUtils.parsePrintAmount(data.getBalance()));
    }

    @Override
    public void updateMemberInfoSuc(String idCard, String cardNo) {
        showMsg("会员信息修改成功");
        if (!StringUtils.isEmpty(idCard)) {
            mData.setIdCard(idCard);
        }
        if (!StringUtils.isEmpty(cardNo)) {
            mData.setCardNo(cardNo);
            EventBus.getDefault().post(new Event.ModifyCardNoEvent(cardNo));
        }
    }

    @Override
    public void refundCardSuc() {
        mRefundCard = true;
        showMsg("退卡成功");
        finish();
    }

    @Override
    public void finish() {
        if (mData != null) {
            Intent intent = new Intent();
            intent.putExtra(Constants.IT_POSITION, mPosition);
            intent.putExtra(Constants.IT_CUSTOMER_ID, mCustomerId);
            intent.putExtra(Constants.IT_CARD_NO, mCardNo);
            intent.putExtra(Constants.IT_REFUND_CARD, mRefundCard);
            intent.putExtra(Constants.IT_ID_CARD, mData.getIdCard());
            intent.putExtra(Constants.IT_NEW_CARD_NO, mData.getCardNo());
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    public String getCardNo() {
        return mData == null ? mCardNo : mData.getCardNo();
    }
}
