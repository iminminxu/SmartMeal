package com.ecare.smartmeal.model.rxjava;

import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.exception.ApiException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/11.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * RxUtils
 */
public class RxUtils {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {   //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<BaseResponse<T>, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<BaseResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<BaseResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<BaseResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(BaseResponse<T> baseResponse) {
                        if (baseResponse.getCode() == Constants.API_SUCCESS) {
                            return createData(baseResponse.getData());
                        } else {
                            return Flowable.error(new ApiException(baseResponse.getMsg(), baseResponse.getCode()));
                        }
                    }
                });
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param t
     * @param <T>
     * @return
     */
    private static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
