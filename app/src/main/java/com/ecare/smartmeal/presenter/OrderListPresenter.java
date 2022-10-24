package com.ecare.smartmeal.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.PagingPresenter;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.contract.OrderListContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.api.ProjectApiService;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.AllOrderRequest;
import com.ecare.smartmeal.model.bean.req.ConfirmDeliveryRequest;
import com.ecare.smartmeal.model.bean.req.NewOrderRequest;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.model.bean.rsp.LoginRspDTO;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.RiderNewOrdersResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;
import com.ecare.smartmeal.ui.fragment.OrderListFragment;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.ecare.smartmeal.utils.TextSpanBuilder;
import com.sunmi.externalprinterlibrary.api.Status;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Flowable;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/6.
 * Email: iminminxu@gmail.com
 */
public class OrderListPresenter extends PagingPresenter<OrderListContract.View, RiderNewOrdersResponse> implements OrderListContract.Presenter {

    //上下文
    private Context mContext;
    //订单方式
    private int mOrderWay;
    //订单状态
    private int mStatus;

    public OrderListPresenter(@OrderListFragment.OrderWay int orderWay, @OrderListFragment.Status int status) {
        mOrderWay = orderWay;
        mStatus = status;
    }

    @Override
    protected BaseQuickAdapter<RiderNewOrdersResponse, BaseViewHolder> createAdapter() {
        BaseQuickAdapter<RiderNewOrdersResponse, BaseViewHolder> adapter = new BaseQuickAdapter<RiderNewOrdersResponse, BaseViewHolder>(R.layout.item_order) {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                OrderListPresenter.this.mContext = parent.getContext();
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void convert(BaseViewHolder helper, RiderNewOrdersResponse item) {
                if (item == null) {
                    return;
                }
                String title = "";
                switch (mOrderWay) {
                    case OrderListFragment.ORDER_WAY_DELIVERY:
                        title = "期望" + item.getExpectTime() + "送达";
                        break;
                    case OrderListFragment.ORDER_WAY_SHOP:
                        title = "预约时间" + item.getExpectTime();
                        break;
                    case OrderListFragment.ORDER_WAY_BOOKING:
                        title = mStatus == OrderListFragment.STATUS_PENDING_ORDER ? item.getCycle() == 1 ? "按周预订" : "按月预订" : item.getMealsType() == 1 ? "今日午餐" : "今日晚餐";
                        break;
                    default:
                        break;
                }
                String status = "";
                String operate = "";
                switch (mStatus) {
                    case OrderListFragment.STATUS_PENDING_ORDER:
                        status = "等待接单";
                        operate = "立即接单";
                        break;
                    case OrderListFragment.STATUS_PENDING_DELIVERY:
                        status = "已接单";
                        operate = "确认配送";
                        break;
                    case OrderListFragment.STATUS_MEAL_WAITING:
                        status = "等待取餐";
                        operate = "取餐完成";
                        break;
                    case OrderListFragment.STATUS_TO_BE_SERVED:
                        status = "等待配餐";
                        operate = "配餐完成";
                        break;
                    default:
                        break;
                }
                String timeNodeList = item.getTimeNodeList();
                helper.setText(R.id.tv_order_title, title)
                        .setText(R.id.tv_order_status, mOrderWay == OrderListFragment.ORDER_WAY_BOOKING && mStatus == OrderListFragment.STATUS_PENDING_DELIVERY ? "" : status)
                        .setText(R.id.tv_name, item.getConsigneeName())
                        .setText(R.id.tv_id_card, item.getCustomerIdCard())
                        .setText(R.id.tv_mobile, item.getConsigneePhone())
                        .setText(R.id.tv_address, item.getArea())
                        .setGone(R.id.tv_address, mOrderWay == OrderListFragment.ORDER_WAY_SHOP)
                        .setText(R.id.tv_take_meal_way, "取餐方式：" + (item.getEatWay() == 1 ? "堂食" : "打包带走"))
                        .setGone(R.id.tv_take_meal_way, mOrderWay != OrderListFragment.ORDER_WAY_SHOP)
                        .setText(R.id.tv_take_meal_code, "取餐码：" + item.getId())
                        .setGone(R.id.tv_take_meal_code, mOrderWay != OrderListFragment.ORDER_WAY_SHOP)
                        .setGone(R.id.tv_commodity_num, mOrderWay == OrderListFragment.ORDER_WAY_BOOKING)
                        .setGone(R.id.rv_commodity, mOrderWay == OrderListFragment.ORDER_WAY_BOOKING)
                        .setText(R.id.tv_time, "开始配送日期：" + item.getStartTime())
                        .setGone(R.id.tv_time, mOrderWay != OrderListFragment.ORDER_WAY_BOOKING || mStatus != OrderListFragment.STATUS_PENDING_ORDER)
                        .setText(R.id.tv_combo, "预定套餐：" + item.getCommodityName())
                        .setGone(R.id.tv_combo, mOrderWay != OrderListFragment.ORDER_WAY_BOOKING)
                        .setText(R.id.tv_period, "预定时间段：" + (StringUtils.equals(timeNodeList, "[1,2]") ? "午餐 & 晚餐" : StringUtils.equals(timeNodeList, "[1]") ? "午餐" : "晚餐"))
                        .setGone(R.id.tv_period, mOrderWay != OrderListFragment.ORDER_WAY_BOOKING || mStatus != OrderListFragment.STATUS_PENDING_ORDER)
                        .setText(R.id.tv_remark, TextSpanBuilder.create("备注：")
                                .append(StringUtils.isEmpty(item.getRemark()) ? "" : item.getRemark()).foregroundColor(mContext.getResources().getColor(R.color.color_999999))
                                .build())
                        .setText(R.id.tv_income, TextSpanBuilder.create("本单收入：")
                                .append(NumUtils.parseAmount(new BigDecimal(StringUtils.isEmpty(item.getIncome()) ? "0" : item.getIncome()))).sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_16)).foregroundColor(mContext.getResources().getColor(R.color.color_f71413))
                                .build())
                        .setGone(R.id.tv_income, mOrderWay == OrderListFragment.ORDER_WAY_BOOKING && mStatus == OrderListFragment.STATUS_PENDING_DELIVERY)
                        .setGone(R.id.tv_print, true)
                        .setText(R.id.tv_operate, operate);
                if (mOrderWay != OrderListFragment.ORDER_WAY_BOOKING) {
                    RecyclerView rvCommodity = helper.getView(R.id.rv_commodity);
                    rvCommodity.setLayoutManager(new LinearLayoutManager(mContext));
                    BaseQuickAdapter<RiderNewOrdersResponse.OrderDetailsDTO, BaseViewHolder> dishesAdapter = new BaseQuickAdapter<RiderNewOrdersResponse.OrderDetailsDTO, BaseViewHolder>(R.layout.item_order_commodity, item.getOrderDetails()) {
                        @Override
                        protected void convert(BaseViewHolder baseViewHolder, RiderNewOrdersResponse.OrderDetailsDTO orderDetailsDTO) {
                            if (orderDetailsDTO == null) {
                                return;
                            }
                            StringBuilder builder = null;
                            List<RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO> specx = orderDetailsDTO.getSpecx();
                            if (specx != null && specx.size() != 0) {
                                builder = new StringBuilder();
                                for (RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO specxDTO : specx) {
                                    if (specxDTO == null) {
                                        continue;
                                    }
                                    List<RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO.ListDTO> list = specxDTO.getList();
                                    if (list == null || list.size() == 0) {
                                        continue;
                                    }
                                    for (RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO.ListDTO listDTO : list) {
                                        if (listDTO == null) {
                                            continue;
                                        }
                                        builder.append(listDTO.getName()).append(" x").append(listDTO.getNum()).append("\n");
                                    }
                                }
                            }
                            if (builder != null && builder.length() > 0) {
                                builder.deleteCharAt(builder.length() - 1);
                            }
                            baseViewHolder.setText(R.id.tv_name, orderDetailsDTO.getName())
                                    .setText(R.id.tv_quantity, "x" + orderDetailsDTO.getNumber())
                                    .setText(R.id.tv_price, NumUtils.parseAmount(orderDetailsDTO.getSinglePrice()))
                                    .setText(R.id.tv_content, builder == null ? "" : builder.toString())
                                    .setGone(R.id.tv_content, builder == null || builder.length() == 0);
                        }
                    };
                    rvCommodity.setAdapter(dishesAdapter);
                    helper.setText(R.id.tv_commodity_num, "商品（" + dishesAdapter.getItemCount() + "）");
                }
            }
        };
        adapter.addChildClickViewIds(R.id.tv_operate);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                RiderNewOrdersResponse item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (mStatus) {
                    case OrderListFragment.STATUS_PENDING_ORDER:
                        operateOrder(item, 2);
                        break;
                    case OrderListFragment.STATUS_PENDING_DELIVERY:
                        confirmDelivery(item);
                        break;
                    case OrderListFragment.STATUS_MEAL_WAITING:
                        operateOrder(item, 5);
                        break;
                    case OrderListFragment.STATUS_TO_BE_SERVED:
                        operateOrder(item, 20);
                        break;
                    default:
                        break;
                }
            }
        });
        return adapter;
    }

    @Override
    protected void loadData(int showType, int pageIndex) {
        ProjectApiService apiService = ProjectApi.getInstance().getApiService();
        AllOrderRequest allOrderRequest = new AllOrderRequest(mOrderWay, pageIndex, mStatus);
        Flowable<BaseResponse<List<RiderNewOrdersResponse>>> flowable = mOrderWay == OrderListFragment.ORDER_WAY_BOOKING ? apiService.getBookingOrderList(allOrderRequest) : apiService.getOrderList(allOrderRequest);
        addSubscribe(flowable.compose(RxUtils.<BaseResponse<List<RiderNewOrdersResponse>>>rxSchedulerHelper())
                .compose(RxUtils.<List<RiderNewOrdersResponse>>handleResult())
                .subscribeWith(new CommonSubscriber<List<RiderNewOrdersResponse>>(mView, showType) {
                    @Override
                    public void onNext(List<RiderNewOrdersResponse> data) {
                        if (isAttachView()) {
                            setData(showType, pageIndex, data);
                        }
                    }
                })
        );
    }

    private void operateOrder(RiderNewOrdersResponse item, int status) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .operateOrder(new NewOrderRequest(item.getOrderno(), item.getSonOrderNo(), status))
                .compose(RxUtils.<BaseResponse<LoginRspDTO>>rxSchedulerHelper())
                .compose(RxUtils.<LoginRspDTO>handleResult())
                .subscribeWith(new CommonSubscriber<LoginRspDTO>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(LoginRspDTO data) {
                        if (isAttachView()) {
                            //接单时打印小票
                            if (status == 2) {
                                try {
                                    int status = SunmiPrinterApi.getInstance().getPrinterStatus();
                                    if (!SunmiPrinterApi.getInstance().isConnected() || status != Status.RUNNING) {
                                        mView.showMsg("未连接打印机，请确认打印机是否连接！");
                                    } else {
                                        mView.showMsg("请取走小票凭证！");
                                        PrintUtils.printPendingOrder(mOrderWay, item);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mView.showMsg("操作成功");
                            }
                            //刷新订单
                            EventBus.getDefault().post(new Event.OrderListRefreshEvent(mOrderWay));
                        }
                    }
                })
        );
    }

    private void confirmDelivery(RiderNewOrdersResponse item) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        MerchantInfoResponse merchantInfo = App.getInstance().getMerchantInfo();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .confirmDelivery(new ConfirmDeliveryRequest(merchantInfo == null ? 0 : merchantInfo.getLatitude(), merchantInfo == null ? 0 : merchantInfo.getLongitude(), item.getOrderno()))
                .compose(RxUtils.<BaseResponse<LoginRspDTO>>rxSchedulerHelper())
                .compose(RxUtils.<LoginRspDTO>handleResult())
                .subscribeWith(new CommonSubscriber<LoginRspDTO>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(LoginRspDTO data) {
                        if (isAttachView()) {
                            mView.showMsg("操作成功");
                            EventBus.getDefault().post(new Event.OrderListRefreshEvent(mOrderWay));
                        }
                    }
                })
        );
    }

    /**
     * 外包后端没有PageSize
     *
     * @return 如果返回list数量小于1，加载完毕
     */
    @Override
    protected int getPageSize() {
        return 1;
    }

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }
}
