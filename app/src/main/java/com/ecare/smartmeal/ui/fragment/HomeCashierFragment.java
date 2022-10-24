package com.ecare.smartmeal.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.HomeCashierContract;
import com.ecare.smartmeal.facepay.FacePayUtils;
import com.ecare.smartmeal.model.bean.rsp.CommodityCombUnitVo;
import com.ecare.smartmeal.model.bean.rsp.CommodityItem;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;
import com.ecare.smartmeal.presenter.HomeCashierPresenter;
import com.ecare.smartmeal.ui.activity.ConfirmOrderActivity;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.TextSpanBuilder;
import com.ecare.smartmeal.widget.GridSpacingItemDecoration;
import com.ecare.smartmeal.widget.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/13.
 * Email: iminminxu@gmail.com
 */
public class HomeCashierFragment extends RootFragment<HomeCashierContract.Presenter> implements HomeCashierContract.View {

    //已选菜品
    @BindView(R.id.ll_selected_dishes)
    LinearLayout llSelectedDishes;
    @BindView(R.id.rv_selected_dishes)
    RecyclerView rvSelectedDishes;
    private BaseQuickAdapter<CommodityxAllResponseItem, BaseViewHolder> mSelectedDishesAdapter;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    //菜单
    @BindView(R.id.fl_menu)
    FrameLayout flMenu;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    private BaseQuickAdapter<CommodityxAllResponse, BaseViewHolder> mCategoryAdapter;
    @BindView(R.id.rv_dishes)
    RecyclerView rvDishes;
    private BaseQuickAdapter<CommodityItem, BaseViewHolder> mDishesAdapter;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.rv_combo)
    RecyclerView rvCombo;
    private BaseQuickAdapter<CommodityCombUnitVo, BaseViewHolder> mComboAdapter;
    @BindView(R.id.tv_combo_name)
    TextView tvComboName;
    @BindView(R.id.tv_combo_content)
    TextView tvComboContent;
    //点击的菜品
    private CommodityxAllResponseItem mCommodityxAllResponseItem;

    @Override
    protected HomeCashierContract.Presenter createPresenter() {
        return new HomeCashierPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_cashier;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        llSelectedDishes.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), getResources().getDimension(R.dimen.dp_3));
            }
        });
        llSelectedDishes.setClipToOutline(true);
        initSelectedDishesRecyclerView();
        tvTotal.setText(TextSpanBuilder.create("合计：")
                .append("¥0.00").sizeInPx((int) getResources().getDimension(R.dimen.font_16)).foregroundColor(mContext.getResources().getColor(R.color.color_f71413))
                .build());
        flMenu.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), getResources().getDimension(R.dimen.dp_3));
            }
        });
        flMenu.setClipToOutline(true);
        initCategoryRecyclerView();
        initDishesRecyclerView();
        initComboRecyclerView();
        doBusiness();
    }

    private void initSelectedDishesRecyclerView() {
        rvSelectedDishes.setLayoutManager(new LinearLayoutManager(mContext));
        mSelectedDishesAdapter = new BaseQuickAdapter<CommodityxAllResponseItem, BaseViewHolder>(R.layout.item_selected_dishes) {
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
                        .setText(R.id.tv_quantity, String.valueOf(item.getNum()))
                        .setText(R.id.tv_content, builder == null ? "" : builder.toString())
                        .setGone(R.id.tv_content, builder == null)
                        .setText(R.id.tv_price, NumUtils.parseAmount(item.getPrice()));
            }
        };
        mSelectedDishesAdapter.addChildClickViewIds(R.id.iv_add, R.id.iv_reduce);
        mSelectedDishesAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                CommodityxAllResponseItem item = mSelectedDishesAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.iv_add:
                        item.setNum(item.getNum() + 1);
                        mSelectedDishesAdapter.notifyDataSetChanged();
                        break;
                    case R.id.iv_reduce:
                        item.setNum(item.getNum() - 1);
                        if (item.getNum() < 1) {
                            mSelectedDishesAdapter.remove(item);
                        } else {
                            mSelectedDishesAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;
                }
                selectedDishesUpdate();
            }
        });
        rvSelectedDishes.setAdapter(mSelectedDishesAdapter);
        rvSelectedDishes.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_e6e6e6))
                .margin((int) getResources().getDimension(R.dimen.dp_13), (int) getResources().getDimension(R.dimen.dp_13))
                .sizeResId(R.dimen.dp_1)
                .showLastDivider()
                .build());
    }

    private void initCategoryRecyclerView() {
        rvCategory.setLayoutManager(new LinearLayoutManager(mContext));
        mCategoryAdapter = new BaseQuickAdapter<CommodityxAllResponse, BaseViewHolder>(R.layout.item_category) {
            @Override
            protected void convert(BaseViewHolder holder, CommodityxAllResponse item) {
                if (item == null) {
                    return;
                }
                holder.setText(R.id.tv_name, item.getTypeName())
                        .setBackgroundColor(R.id.tv_name, item.isCheck() ? getResources().getColor(R.color.color_ec7220) : Color.TRANSPARENT);
            }
        };
        rvCategory.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                selectCategory(position);
            }
        });
        rvCategory.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_e6e6e6))
                .sizeResId(R.dimen.dp_1)
                .build());
    }

    /**
     * 选择分类
     *
     * @param position 索引
     */
    private void selectCategory(int position) {
        CommodityxAllResponse item = mCategoryAdapter.getItem(position);
        if (item == null) {
            return;
        }
        for (CommodityxAllResponse commodityxAllResponse : mCategoryAdapter.getData()) {
            if (commodityxAllResponse == null) {
                continue;
            }
            commodityxAllResponse.setCheck(false);
        }
        //设置选中并更新适配器
        item.setCheck(true);
        mCategoryAdapter.notifyDataSetChanged();
        //设置商品列表数据
        mDishesAdapter.setNewData(item.getList());
    }

    private void initDishesRecyclerView() {
        rvDishes.setLayoutManager(new GridLayoutManager(mContext, 4));
        mDishesAdapter = new BaseQuickAdapter<CommodityItem, BaseViewHolder>(R.layout.item_dishes) {
            @Override
            protected void convert(BaseViewHolder holder, CommodityItem item) {
                if (item == null) {
                    return;
                }
                int quantity = 0;
                for (CommodityxAllResponseItem commodityxAllResponseItem : mSelectedDishesAdapter.getData()) {
                    if (commodityxAllResponseItem == null) {
                        continue;
                    }
                    if (item.getId() == commodityxAllResponseItem.getId()) {
                        quantity = commodityxAllResponseItem.getNum();
                        break;
                    }
                }
                holder.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_price, NumUtils.parseAmount(item.getPrice()))
                        .setText(R.id.tv_quantity, String.valueOf(quantity))
                        .setVisible(R.id.tv_quantity, quantity > 0);
            }
        };
        rvDishes.setAdapter(mDishesAdapter);
        mDishesAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CommodityItem item = mDishesAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                mCommodityxAllResponseItem = null;
                for (CommodityxAllResponseItem datum : mSelectedDishesAdapter.getData()) {
                    if (datum == null) {
                        continue;
                    }
                    if (item.getId() == datum.getId()) {
                        mCommodityxAllResponseItem = datum;
                        break;
                    }
                }
                if (mCommodityxAllResponseItem == null) {
                    mCommodityxAllResponseItem = new CommodityxAllResponseItem();
                    mCommodityxAllResponseItem.setId(item.getId());
                    mCommodityxAllResponseItem.setName(item.getName());
                    mCommodityxAllResponseItem.setNum(1);
                    mCommodityxAllResponseItem.setPacking(item.getPacking());
                    mCommodityxAllResponseItem.setPrice(item.getPrice());
                    mCommodityxAllResponseItem.setSingleType(item.getSingleType());
                    mCommodityxAllResponseItem.setSpec(item.getSpec());
                    mCommodityxAllResponseItem.setTaboos(item.getTaboos());
                    mCommodityxAllResponseItem.setType(item.getType());
                    if (item.getType() == 2) {
                        //选择套餐
                        mPresenter.getCommodityUnit(item.getId(), item.getMerchantId());
                    } else {
                        mSelectedDishesAdapter.addData(mCommodityxAllResponseItem);
                        selectedDishesUpdate();
                    }
                } else {
                    mCommodityxAllResponseItem.setNum(mCommodityxAllResponseItem.getNum() + 1);
                    mSelectedDishesAdapter.notifyDataSetChanged();
                    selectedDishesUpdate();
                }
            }
        });
        rvDishes.addItemDecoration(new GridSpacingItemDecoration(4, (int) getResources().getDimension(R.dimen.dp_4), true));
    }

    private void initComboRecyclerView() {
        rvCombo.setLayoutManager(new LinearLayoutManager(mContext));
        mComboAdapter = new BaseQuickAdapter<CommodityCombUnitVo, BaseViewHolder>(R.layout.item_combo_category) {
            @Override
            protected void convert(BaseViewHolder holder, CommodityCombUnitVo item) {
                if (item == null) {
                    return;
                }
                List<CommodityItem> list = item.getList();
                int selectNum = 0;
                if (list != null && list.size() != 0) {
                    for (CommodityItem commodityItem : list) {
                        if (commodityItem == null) {
                            continue;
                        }
                        selectNum = selectNum + commodityItem.getNum();
                    }
                }
                if (item.getUserNum() - selectNum > 0) {
                    holder.setText(R.id.tv_name, "选" + item.getSingleName() + "(剩余 " + (item.getUserNum() - selectNum) + " 份未选)");
                } else {
                    holder.setText(R.id.tv_name, "选" + item.getSingleName());
                }
                RecyclerView rvComboDishes = holder.getView(R.id.rv_combo_dishes);
                rvComboDishes.setLayoutManager(new GridLayoutManager(mContext, 4));
                BaseQuickAdapter<CommodityItem, BaseViewHolder> comboCommodityAdapter = new BaseQuickAdapter<CommodityItem, BaseViewHolder>(R.layout.item_combo_dishes, list) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, CommodityItem commodityItem) {
                        if (commodityItem == null) {
                            return;
                        }
                        baseViewHolder.setText(R.id.tv_name, commodityItem.getName())
                                .setText(R.id.tv_price, NumUtils.parseAmount(commodityItem.getPrice()))
                                .setText(R.id.tv_quantity, String.valueOf(commodityItem.getNum()))
                                .setVisible(R.id.tv_quantity, commodityItem.getNum() > 0);
                    }
                };
                comboCommodityAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        CommodityItem commodityItem = comboCommodityAdapter.getItem(position);
                        if (commodityItem == null) {
                            return;
                        }
                        int selectNum = 0;
                        for (CommodityItem datum : comboCommodityAdapter.getData()) {
                            if (datum == null) {
                                continue;
                            }
                            selectNum = selectNum + datum.getNum();
                        }
                        int userNum = item.getUserNum();
                        if (userNum > selectNum) {
                            commodityItem.setNum(commodityItem.getNum() + 1);
                            mComboAdapter.notifyDataSetChanged();
                            updateComboContent();
                        } else {
                            showMsg("最多可选" + userNum + "份");
                        }
                    }
                });
                rvComboDishes.setAdapter(comboCommodityAdapter);
                if (rvComboDishes.getTag() == null) {
                    rvComboDishes.addItemDecoration(new GridSpacingItemDecoration(4, (int) getResources().getDimension(R.dimen.dp_4), false));
                    rvComboDishes.setTag("Add");
                }
            }
        };
        mComboAdapter.addChildClickViewIds(R.id.tv_clear);
        mComboAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CommodityCombUnitVo item = mComboAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                List<CommodityItem> list = item.getList();
                if (list != null && list.size() != 0) {
                    for (CommodityItem commodityItem : list) {
                        if (commodityItem == null) {
                            continue;
                        }
                        commodityItem.setNum(0);
                    }
                }
                mComboAdapter.notifyDataSetChanged();
                updateComboContent();
            }
        });
        rvCombo.setAdapter(mComboAdapter);
        rvCombo.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.dp_13)
                .showLastDivider()
                .build());
    }

    private void updateComboContent() {
        StringBuilder builder = new StringBuilder();
        for (CommodityCombUnitVo commodityCombUnitVo : mComboAdapter.getData()) {
            if (commodityCombUnitVo == null) {
                continue;
            }
            List<CommodityItem> list = commodityCombUnitVo.getList();
            if (list == null || list.size() == 0) {
                continue;
            }
            for (CommodityItem commodityItem : list) {
                if (commodityItem == null) {
                    continue;
                }
                int num = commodityItem.getNum();
                if (num > 0) {
                    builder.append(commodityItem.getName()).append(" x").append(num).append("  ");
                }
            }
        }
        tvComboContent.setText(builder.toString());
    }

    @OnClick({R.id.tv_clear, R.id.tv_confirm, R.id.dtv_combo_back, R.id.tv_combo_default, R.id.tv_combo_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                mSelectedDishesAdapter.getData().clear();
                mSelectedDishesAdapter.notifyDataSetChanged();
                selectedDishesUpdate();
                break;
            case R.id.tv_confirm:
                List<CommodityxAllResponseItem> data = mSelectedDishesAdapter.getData();
                if (data.size() == 0) {
                    showMsg("请选择菜品");
                    break;
                }
                Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                intent.putParcelableArrayListExtra(Constants.IT_SELECTED_DISHES, (ArrayList<? extends Parcelable>) data);
                startActivityForResult(intent, Constants.REQ_CONFIRM_ORDER);
                break;
            case R.id.dtv_combo_back:
                flMenu.getChildAt(0).setVisibility(View.VISIBLE);
                flMenu.getChildAt(1).setVisibility(View.GONE);
                break;
            case R.id.tv_combo_default:
                mSelectedDishesAdapter.addData(mCommodityxAllResponseItem);
                selectedDishesUpdate();
                flMenu.getChildAt(0).setVisibility(View.VISIBLE);
                flMenu.getChildAt(1).setVisibility(View.GONE);
                break;
            case R.id.tv_combo_confirm:
                addComboCommodity();
                break;
            default:
                break;
        }
    }

    private void addComboCommodity() {
        List<CommodityItem> selectComboCommodity = new ArrayList<>();
        for (CommodityCombUnitVo commodityCombUnitVo : mComboAdapter.getData()) {
            if (commodityCombUnitVo == null) {
                continue;
            }
            List<CommodityItem> list = commodityCombUnitVo.getList();
            if (list == null || list.size() == 0) {
                continue;
            }
            int selectNum = 0;
            for (CommodityItem commodityItem : list) {
                if (commodityItem == null) {
                    continue;
                }
                int num = commodityItem.getNum();
                if (num > 0) {
                    selectComboCommodity.add(commodityItem);
                    selectNum = selectNum + num;
                }
            }
            if (selectNum < commodityCombUnitVo.getUserNum()) {
                showMsg("未选择全部菜品");
                return;
            }
        }
        mCommodityxAllResponseItem.setCommodityComb(selectComboCommodity);
        mSelectedDishesAdapter.addData(mCommodityxAllResponseItem);
        selectedDishesUpdate();
        flMenu.getChildAt(0).setVisibility(View.VISIBLE);
        flMenu.getChildAt(1).setVisibility(View.GONE);
    }

    /**
     * 已选菜品更新(更新菜单，合计，SKU)
     */
    private void selectedDishesUpdate() {
        mDishesAdapter.notifyDataSetChanged();
        BigDecimal totalAmount = new BigDecimal("0");
        for (CommodityxAllResponseItem commodityxAllResponseItem : mSelectedDishesAdapter.getData()) {
            if (commodityxAllResponseItem == null) {
                continue;
            }
            totalAmount = totalAmount.add(commodityxAllResponseItem.getPrice().multiply(new BigDecimal(String.valueOf(commodityxAllResponseItem.getNum()))));
        }
        tvTotal.setText(TextSpanBuilder.create("合计：")
                .append(NumUtils.parseAmount(totalAmount)).sizeInPx((int) getResources().getDimension(R.dimen.font_16)).foregroundColor(mContext.getResources().getColor(R.color.color_f71413))
                .build());
        //同时更新C屏
        FacePayUtils.showOrHideSKU(mSelectedDishesAdapter.getData(), new BigDecimal("0"), totalAmount, totalAmount, null);
    }

    @Override
    protected void doBusiness() {
        //获取全部商品
        stateLoading();
        mPresenter.getAllCommodity();
    }

    @Override
    protected void firstLoadDataFailRetry() {
        //获取全部商品
        stateLoading();
        mPresenter.getAllCommodity();
    }

    @Override
    public void setAllCommodity(List<CommodityxAllResponse> data) {
        if (data == null || data.size() == 0) {
            rvCategory.setVisibility(View.GONE);
            rvDishes.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        } else {
            rvCategory.setVisibility(View.VISIBLE);
            rvDishes.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mCategoryAdapter.setNewData(data);
            selectCategory(0);
        }
    }

    @Override
    public void setCommodityUnit(int commodityId, List<CommodityCombUnitVo> data) {
        if (mCommodityxAllResponseItem == null || mCommodityxAllResponseItem.getId() != commodityId) {
            return;
        }
        flMenu.getChildAt(0).setVisibility(View.GONE);
        flMenu.getChildAt(1).setVisibility(View.VISIBLE);
        mComboAdapter.setNewData(data);
        tvComboName.setText(mCommodityxAllResponseItem.getName());
        tvComboContent.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.REQ_CONFIRM_ORDER:
                mSelectedDishesAdapter.getData().clear();
                mSelectedDishesAdapter.notifyDataSetChanged();
                selectedDishesUpdate();
                break;
        }
    }
}
