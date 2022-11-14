package com.ecare.smartmeal.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.ecare.smartmeal.R;
import com.lxj.xpopup.core.CenterPopupView;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/7.
 * Email: iminminxu@gmail.com
 */
public class DiscountPopup extends CenterPopupView {

    private EditText etDiscount;
    private OnDiscountListener listener;

    public DiscountPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_discount;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        etDiscount = findViewById(R.id.et_discount);
        findViewById(R.id.tv_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String discount = etDiscount.getText().toString();
                try {
                    BigDecimal bigDecimal = new BigDecimal(discount);
                    if (listener != null) {
                        listener.onDiscount(bigDecimal);
                    }
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShort("请输入正确的折扣！");
                }
            }
        });
        findViewById(R.id.tv_six).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDiscount(new BigDecimal("6"));
                }
                dismiss();
            }
        });
        findViewById(R.id.tv_five).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDiscount(new BigDecimal("5"));
                }
                dismiss();
            }
        });
        findViewById(R.id.tv_free).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDiscount(new BigDecimal("0"));
                }
                dismiss();
            }
        });
    }

    public interface OnDiscountListener {
        void onDiscount(BigDecimal discount);
    }

    public DiscountPopup setOnDiscountListener(OnDiscountListener listener) {
        this.listener = listener;
        return this;
    }
}
