package com.ecare.smartmeal.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseActivity;
import com.ecare.smartmeal.contract.MemberAddContract;
import com.ecare.smartmeal.model.bean.rsp.CustomerInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.JsonBean;
import com.ecare.smartmeal.presenter.MemberAddPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/17.
 * Email: iminminxu@gmail.com
 */
public class MemberAddActivity extends BaseActivity<MemberAddContract.Presenter> implements MemberAddContract.View {

    //标题
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //新增会员信息
    @BindView(R.id.et_id_card)
    EditText etIdCard;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_address_detail)
    EditText etAddressDetail;
    @BindView(R.id.et_card_no)
    EditText etCardNo;
    //选择地区弹框
    private OptionsPickerView mPickerView;
    //选择的省市区
    private String mProvince;
    private String mCity;
    private String mDistrict;
    //输入监听
    private TextWatcher mIDCardWatcher;
    private TextWatcher mMobileWatcher;

    @Override
    protected MemberAddContract.Presenter createPresenter() {
        return new MemberAddPresenter();
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_member_add;
    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        //设置标题
        tvTitle.setText("新增会员用户");
        //设置输入监听
        mIDCardWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String iDCard = s.toString();
                if (RegexUtils.isIDCard18(iDCard)) {
                    mPresenter.findCustomer(iDCard, "");
                }
            }
        };
        etIdCard.addTextChangedListener(mIDCardWatcher);
        mMobileWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mobile = s.toString();
                if (RegexUtils.isMobileSimple(mobile)) {
                    mPresenter.findCustomer("", mobile);
                }
            }
        };
        etMobile.addTextChangedListener(mMobileWatcher);
        //设置默认地址
        mProvince = "浙江省";
        mCity = "杭州市";
        mDistrict = "西湖区";
        tvAddress.setText(mProvince + mCity + mDistrict);
    }

    @OnClick({R.id.dtv_back, R.id.tv_address, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_back:
                finish();
                break;
            case R.id.tv_address:
                if (mPickerView == null) {
                    mPresenter.getAddress();
                } else {
                    mPickerView.show();
                }
                break;
            case R.id.tv_confirm:
                String iDCard = etIdCard.getText().toString();
                if (!RegexUtils.isIDCard18(iDCard)) {
                    showMsg("请输入正确的身份证号");
                    break;
                }
                String name = etName.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    showMsg("请输入姓名");
                    break;
                }
                String mobile = etMobile.getText().toString();
                if (!RegexUtils.isMobileSimple(mobile)) {
                    showMsg("请输入正确的联系电话");
                    break;
                }
                String address = tvAddress.getText().toString();
                if (StringUtils.isEmpty(address)) {
                    showMsg("请选择省市区");
                    break;
                }
                String addressDetail = etAddressDetail.getText().toString();
                if (StringUtils.isEmpty(addressDetail)) {
                    showMsg("请输入详细地址");
                    break;
                }
                String cardNo = etCardNo.getText().toString();
                if (StringUtils.isEmpty(cardNo)) {
                    showMsg("请刷卡读取卡号");
                    break;
                }
                mPresenter.addMember(iDCard, name, mobile, mProvince, mCity, mDistrict, addressDetail, cardNo);
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public void showPickerView(ArrayList<JsonBean> options1Items, ArrayList<ArrayList<JsonBean.CityBean>> options2Items, ArrayList<ArrayList<ArrayList<JsonBean.AreaBean>>> options3Items) {
        mPickerView = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            mProvince = options1Items.get(options1).getName();
            mCity = options2Items.get(options1).get(options2).getName();
            mDistrict = options3Items.get(options1).get(options2).get(options3).getName();
            tvAddress.setText(mProvince + mCity + mDistrict);
        })
                .setTitleText("请选择省市区")
                .setDecorView(getWindow().getDecorView().findViewById(android.R.id.content))
                .build();
        mPickerView.setPicker(options1Items, options2Items, options3Items);
        mPickerView.show();
    }

    @Override
    public void addMemberSuc() {
        showMsg("新增会员用户成功");
        finish();
    }

    @Override
    public void findCustomerSuc(CustomerInfoResponse data) {
        if (data == null || StringUtils.isEmpty(data.getMobile())) {
            return;
        }
        //移除监听，防止无限循环
        etIdCard.removeTextChangedListener(mIDCardWatcher);
        etMobile.removeTextChangedListener(mMobileWatcher);
        //设置默认填充数据
        etIdCard.setText(data.getIdCard());
        etName.setText(data.getName());
        etMobile.setText(data.getMobile());
        mProvince = data.getLiveProvincex();
        mCity = data.getLiveCityx();
        mDistrict = data.getLiveAreasx();
        tvAddress.setText(mProvince + mCity + mDistrict);
        etAddressDetail.setText(data.getLiveAddrx());
        //禁止输入
        etIdCard.setEnabled(false);
        etIdCard.setBackgroundResource(R.drawable.corners_3_solid_f8f9fb_stroke_1_c5c8cb);
        etName.setEnabled(false);
        etName.setBackgroundResource(R.drawable.corners_3_solid_f8f9fb_stroke_1_c5c8cb);
        etMobile.setEnabled(false);
        etMobile.setBackgroundResource(R.drawable.corners_3_solid_f8f9fb_stroke_1_c5c8cb);
        tvAddress.setEnabled(false);
        tvAddress.setBackgroundResource(R.drawable.corners_3_solid_f8f9fb_stroke_1_c5c8cb);
        etAddressDetail.setEnabled(false);
        etAddressDetail.setBackgroundResource(R.drawable.corners_3_solid_f8f9fb_stroke_1_c5c8cb);
    }
}
