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
import com.ecare.smartmeal.contract.CashierOrderListContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.OrderListRequest;
import com.ecare.smartmeal.model.bean.req.RefundOrderRequest;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.model.bean.rsp.OrderCountResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderListResponse;
import com.ecare.smartmeal.model.bean.rsp.PrintTicketResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;
import com.ecare.smartmeal.ui.activity.MainActivity;
import com.ecare.smartmeal.ui.fragment.OrderListNewFragment;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.ecare.smartmeal.utils.TextSpanBuilder;
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
public class CashierOrderListPresenter extends PagingPresenter<CashierOrderListContract.View, OrderListResponse> implements CashierOrderListContract.Presenter {

    //上下文
    private Context mContext;
    //页面传递参数
    private int mSource;
    private int mStatus;

    public CashierOrderListPresenter(int source, int status) {
        mSource = source;
        mStatus = status;
    }

    @Override
    protected BaseQuickAdapter<OrderListResponse, BaseViewHolder> createAdapter() {
        BaseQuickAdapter<OrderListResponse, BaseViewHolder> adapter = new BaseQuickAdapter<OrderListResponse, BaseViewHolder>(R.layout.item_order) {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                CashierOrderListPresenter.this.mContext = parent.getContext();
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void convert(BaseViewHolder helper, OrderListResponse item) {
                if (item == null) {
                    return;
                }
                helper.setText(R.id.tv_order_title, "订单编号：" + item.getOrderno())
                        .setText(R.id.tv_order_status, mStatus == OrderListNewFragment.STATUS_COMPLETED ? "已完成" : "已退单")
                        .setText(R.id.tv_name, "创建时间：" + item.getCreatTime())
                        .setGone(R.id.tv_id_card, true)
                        .setGone(R.id.tv_mobile, true)
                        .setGone(R.id.tv_address, true)
                        .setText(R.id.tv_take_meal_way, "手机号：" + MainActivity.desensitizedPhoneNumber(item.getMobile()))
                        .setText(R.id.tv_take_meal_code, "用户名称：" + item.getName())
                        .setGone(R.id.tv_time, true)
                        .setGone(R.id.tv_combo, true)
                        .setGone(R.id.tv_period, true)
                        .setGone(R.id.tv_remark, true)
                        .setText(R.id.tv_income, TextSpanBuilder.create("本单收入：")
                                .append(NumUtils.parseAmount(item.getAmount())).sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_16)).foregroundColor(mContext.getResources().getColor(R.color.color_f71413))
                                .build())
                        .setGone(R.id.tv_print, mStatus == OrderListNewFragment.STATUS_REFUNDED)
                        .setGone(R.id.tv_operate, mStatus == OrderListNewFragment.STATUS_REFUNDED || !StringUtils.equals(item.getPayType(), "1"))
                        .setText(R.id.tv_operate, "发起退款");
                RecyclerView rvCommodity = helper.getView(R.id.rv_commodity);
                rvCommodity.setLayoutManager(new LinearLayoutManager(mContext));
                BaseQuickAdapter<OrderListResponse.OrderDetailsDTO, BaseViewHolder> dishesAdapter = new BaseQuickAdapter<OrderListResponse.OrderDetailsDTO, BaseViewHolder>(R.layout.item_order_commodity, item.getOrderDetails()) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, OrderListResponse.OrderDetailsDTO orderDetailsDTO) {
                        if (orderDetailsDTO == null) {
                            return;
                        }
                        StringBuilder builder = null;
                        List<OrderListResponse.OrderDetailsDTO.ItemListDTO> itemList = orderDetailsDTO.getItemList();
                        if (itemList != null && itemList.size() != 0) {
                            builder = new StringBuilder();
                            for (OrderListResponse.OrderDetailsDTO.ItemListDTO listDTO : itemList) {
                                if (listDTO == null) {
                                    continue;
                                }
                                builder.append(listDTO.getName()).append(" x").append(listDTO.getNum()).append("\n");
                            }
                        }
                        if (builder != null && builder.length() > 0) {
                            builder.deleteCharAt(builder.length() - 1);
                        }
                        baseViewHolder.setText(R.id.tv_name, orderDetailsDTO.getName())
                                .setText(R.id.tv_quantity, "x" + orderDetailsDTO.getNumber())
                                .setText(R.id.tv_content, builder == null ? "" : builder.toString())
                                .setGone(R.id.tv_content, builder == null || builder.length() == 0);
                    }
                };
                rvCommodity.setAdapter(dishesAdapter);
                helper.setText(R.id.tv_commodity_num, "商品（" + dishesAdapter.getItemCount() + "）");
            }
        };
        adapter.addChildClickViewIds(R.id.tv_print, R.id.tv_operate);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                OrderListResponse item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.tv_print:
                        if (mStatus == OrderListNewFragment.STATUS_REFUNDED) {
                            break;
                        }
                        new XPopup.Builder(mContext).asConfirm("提示", "确定补打小票？", "取消", "确认", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                printTicket(item);
                            }
                        }, null, false).show();
                        break;
                    case R.id.tv_operate:
                        if (mStatus == OrderListNewFragment.STATUS_REFUNDED || !StringUtils.equals(item.getPayType(), "1")) {
                            break;
                        }
                        new XPopup.Builder(mContext).asConfirm("提示", "确定发起退单？", "取消", "确认", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                refundOrder(item.getOrderno());
                            }
                        }, null, false).show();
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

    @Override
    public void getOrderCount() {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getOrderCount()
                .compose(RxUtils.<BaseResponse<OrderCountResponse>>rxSchedulerHelper())
                .compose(RxUtils.<OrderCountResponse>handleResult())
                .subscribeWith(new CommonSubscriber<OrderCountResponse>(mView) {
                    @Override
                    public void onNext(OrderCountResponse data) {
                        if (isAttachView()) {
                            mView.setOrderCount(data);
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
                                    PrintUtils.printDineInOrder(item, data);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
        );
    }

    private void refundOrder(String orderno) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .refundOrder(new RefundOrderRequest(orderno))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.showMsg("退款成功");
                            EventBus.getDefault().post(new Event.OrderListNewRefreshEvent(mSource));
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
