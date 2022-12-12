package com.ecare.smartmeal.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BasePagingView;
import com.ecare.smartmeal.base.PagingPresenter;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.ChangeOrderStatusRequest;
import com.ecare.smartmeal.model.bean.req.CustomerRequest;
import com.ecare.smartmeal.model.bean.req.OrderListRequest;
import com.ecare.smartmeal.model.bean.req.RefundOrderRequest;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.model.bean.rsp.OrderListResponse;
import com.ecare.smartmeal.model.bean.rsp.PrintTicketResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;
import com.ecare.smartmeal.ui.fragment.OrderListNewFragment;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.ecare.smartmeal.utils.TextSpanBuilder;
import com.ecare.smartmeal.widget.ImproveInfoPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.sunmi.externalprinterlibrary.api.Status;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/6.
 * Email: iminminxu@gmail.com
 */
public class OrderListNewPresenter extends PagingPresenter<BasePagingView, OrderListResponse> {

    //上下文
    private Context mContext;
    //页面传递参数
    private int mSource;
    private int mStatus;

    public OrderListNewPresenter(int source, int status) {
        mSource = source;
        mStatus = status;
    }

    @Override
    protected BaseQuickAdapter<OrderListResponse, BaseViewHolder> createAdapter() {
        BaseQuickAdapter<OrderListResponse, BaseViewHolder> adapter = new BaseQuickAdapter<OrderListResponse, BaseViewHolder>(R.layout.item_order_new) {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                OrderListNewPresenter.this.mContext = parent.getContext();
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void convert(BaseViewHolder helper, OrderListResponse item) {
                if (item == null) {
                    return;
                }
                helper.setText(R.id.tv_order_no, "订单编号：" + item.getOrderno())
                        .setText(R.id.tv_order_time, TextSpanBuilder.create("下单时间：")
                                .append(item.getCreatTime()).bold()
                                .build())
                        .setText(R.id.tv_order_type, "华数TV")
                        .setText(R.id.tv_name, item.getName())
                        .setGone(R.id.tv_name, StringUtils.isEmpty(item.getIdCard()))
                        .setText(R.id.tv_mobile, item.getMobile())
                        .setText(R.id.tv_address, item.getLiveAddr())
                        .setGone(R.id.tv_address, StringUtils.isEmpty(item.getIdCard()))
                        .setGone(R.id.tv_improve_info, !StringUtils.isEmpty(item.getIdCard()))
                        .setText(R.id.tv_income, TextSpanBuilder.create("预估收入：").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_12)).foregroundColor(mContext.getResources().getColor(R.color.color_333333))
                                .append(NumUtils.parseAmount(item.getAmount()))
                                .build());
                //设置商品
                List<OrderListResponse.OrderDetailsDTO> orderDetails = item.getOrderDetails();
                if (orderDetails == null || orderDetails.size() == 0) {
                    helper.setText(R.id.tv_commodity_title, TextSpanBuilder.create("商品")
                            .append("（0）").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_13))
                            .build())
                            .setText(R.id.tv_commodity, "");
                } else {
                    helper.setText(R.id.tv_commodity_title, TextSpanBuilder.create("商品")
                            .append("（").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_13))
                            .append(String.valueOf(item.getOrderDetails().size())).sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_13))
                            .append("）").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_13))
                            .build());
                    StringBuilder builder = new StringBuilder();
                    for (OrderListResponse.OrderDetailsDTO orderDetail : orderDetails) {
                        if (orderDetail == null) {
                            continue;
                        }
                        builder.append(TextSpanBuilder.create(orderDetail.getName()).bold()
                                .append(" x").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_12)).foregroundColor(mContext.getResources().getColor(R.color.color_666666))
                                .append(String.valueOf(orderDetail.getNumber())).sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_12)).foregroundColor(mContext.getResources().getColor(R.color.color_666666))
                                .append("  ")
                                .append(NumUtils.parseAmount(orderDetail.getSinglePrice())).bold()
                                .build());
                    }
                    helper.setText(R.id.tv_commodity, builder);
                }
                //设置操作按钮
                String operate = "";
                switch (mStatus) {
                    case OrderListNewFragment.STATUS_COMPLETED:
                        operate = "补打小票";
                        break;
                    case OrderListNewFragment.STATUS_PENDING_ORDER:
                        operate = "立即接单";
                        break;
                    case OrderListNewFragment.STATUS_PENDING_DELIVERY:
                        operate = "打印配送小票";
                        break;
                    default:
                        break;
                }
                helper.setText(R.id.tv_operate, operate);
            }
        };
        adapter.addChildClickViewIds(R.id.tv_improve_info, R.id.tv_operate);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                OrderListResponse item = mAdapter.getItem(position);
                if (item == null || !isAttachView()) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.tv_improve_info:
                        ((ImproveInfoPopup) new XPopup.Builder(mContext)
                                .isDestroyOnDismiss(true)
                                .autoOpenSoftInput(true)
                                .asCustom(new ImproveInfoPopup(mContext, item.getMobile())))
                                .setOnImproveInfoListener(new ImproveInfoPopup.OnImproveInfoListener() {
                                    @Override
                                    public void onImproveInfo(String name, String iDCard, String mobile, String province, String city, String district, String addressDetail) {
                                        updateElderInfo(name, iDCard, mobile, province, city, district, addressDetail);
                                    }
                                })
                                .show();
                        break;
                    case R.id.tv_operate:
                        if (mStatus == OrderListNewFragment.STATUS_PENDING_ORDER) {
                            //待接单
                            new XPopup.Builder(mContext).asConfirm("提示", "确定接单？", "取消", "确认", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    changeOrderStatus(item.getOrderno(), OrderListNewFragment.STATUS_PENDING_DELIVERY);
                                }
                            }, null, false).show();
                        } else {
                            //打印小票
                            new XPopup.Builder(mContext).asConfirm("提示", "确定打印小票？", "取消", "确认", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    printTicket(item);
                                }
                            }, null, false).show();
                        }
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
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getCashierOrderList(new OrderListRequest(pageIndex, mPageSize, mSource, mStatus))
                .compose(RxUtils.<BaseResponse<List<OrderListResponse>>>rxSchedulerHelper())
                .compose(RxUtils.<List<OrderListResponse>>handleResult())
                .subscribeWith(new CommonSubscriber<List<OrderListResponse>>(mView, showType) {
                    @Override
                    public void onNext(List<OrderListResponse> data) {
                        if (isAttachView()) {
                            setData(showType, pageIndex, data);
                        }
                    }
                })
        );
    }

    private void updateElderInfo(String name, String iDCard, String mobile, String province, String city, String district, String addressDetail) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .updateElderInfo(new CustomerRequest("", iDCard, addressDetail, district, city, province, mobile, name))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.showMsg("完善老人信息成功");
                            EventBus.getDefault().post(new Event.OrderListNewRefreshEvent(mSource));
                        }
                    }
                })
        );
    }

    private void changeOrderStatus(String orderNo, int status) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .changeOrderStatus(new ChangeOrderStatusRequest(orderNo, status))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.showMsg("操作成功");
                            EventBus.getDefault().post(new Event.OrderListNewRefreshEvent(mSource));
                        }
                    }
                })
        );
    }

    private void printTicket(OrderListResponse item) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .printTicket(new RefundOrderRequest(item.getOrderno()))
                .compose(RxUtils.<BaseResponse<PrintTicketResponse>>rxSchedulerHelper())
                .compose(RxUtils.<PrintTicketResponse>handleResult())
                .subscribeWith(new CommonSubscriber<PrintTicketResponse>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(PrintTicketResponse data) {
                        if (isAttachView()) {
                            try {
                                int status = SunmiPrinterApi.getInstance().getPrinterStatus();
                                if (!SunmiPrinterApi.getInstance().isConnected() || status != Status.RUNNING) {
                                    mView.showMsg("未连接打印机，请确认打印机是否连接！");
                                } else {
                                    mView.showMsg("请取走小票凭证！");
                                    PrintUtils.printPendingOrder(item, data);
                                    if (mStatus == OrderListNewFragment.STATUS_PENDING_DELIVERY) {
                                        changeOrderStatus(item.getOrderno(), OrderListNewFragment.STATUS_COMPLETED);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }
}
