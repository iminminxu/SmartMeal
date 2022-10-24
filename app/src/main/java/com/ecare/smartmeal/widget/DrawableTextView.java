package com.ecare.smartmeal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ecare.smartmeal.R;

/**
 * ProjectName: NeighborhoodLongevity
 * Package: com.ecare.neighborhoodlongevity.widght
 * ClassName: DrawableTextView
 * Description:
 * Author:
 * CreateDate: 2021/11/23 15:28
 * Version: 1.0
 */
public class DrawableTextView extends TextView {

    private float mDrawableLeftWidth;
    private float mDrawableLeftHeight;
    private float mDrawableRightWidth;
    private float mDrawableRightHeight;
    private float mDrawableTopWidth;
    private float mDrawableTopHeight;
    private float mDrawableBottomWidth;
    private float mDrawableBottomHeight;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        if (attr == null) {
            //不设置Drawable，按照父类实现。
            return;
        }
        try {
            mDrawableLeftWidth = attr.getDimension(R.styleable.DrawableTextView_drawableLeftWidth, 0);
            mDrawableLeftHeight = attr.getDimension(R.styleable.DrawableTextView_drawableLeftHeight, 0);
            mDrawableRightWidth = attr.getDimension(R.styleable.DrawableTextView_drawableRightWidth, 0);
            mDrawableRightHeight = attr.getDimension(R.styleable.DrawableTextView_drawableRightHeight, 0);
            mDrawableTopWidth = attr.getDimension(R.styleable.DrawableTextView_drawableTopWidth, 0);
            mDrawableTopHeight = attr.getDimension(R.styleable.DrawableTextView_drawableTopHeight, 0);
            mDrawableBottomWidth = attr.getDimension(R.styleable.DrawableTextView_drawableBottomWidth, 0);
            mDrawableBottomHeight = attr.getDimension(R.styleable.DrawableTextView_drawableBottomHeight, 0);
        } finally {
            attr.recycle();
        }
        setDrawable();
    }

    private void setDrawable() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        //设置左边Drawable大小
        Drawable drawableLeft = compoundDrawables[0];
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, (int) mDrawableLeftWidth, (int) mDrawableLeftHeight);
        }
        //设置右边Drawable大小
        Drawable drawableRight = compoundDrawables[2];
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, (int) mDrawableRightWidth, (int) mDrawableRightHeight);
        }
        //设置顶部Drawable大小
        Drawable drawableTop = compoundDrawables[1];
        if (drawableTop != null) {
            drawableTop.setBounds(0, 0, (int) mDrawableTopWidth, (int) mDrawableTopHeight);
        }
        //设置底部Drawable大小
        Drawable drawableBottom = compoundDrawables[3];
        if (drawableBottom != null) {
            drawableBottom.setBounds(0, 0, (int) mDrawableBottomWidth, (int) mDrawableBottomHeight);
        }
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        final Context context = getContext();
        //设置左边Drawable大小
        Drawable drawableLeft = left != 0 ? context.getDrawable(left) : null;
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, (int) mDrawableLeftWidth, (int) mDrawableLeftHeight);
        }
        //设置右边Drawable大小
        Drawable drawableRight = right != 0 ? context.getDrawable(right) : null;
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, (int) mDrawableRightWidth, (int) mDrawableRightHeight);
        }
        //设置顶部Drawable大小
        Drawable drawableTop = top != 0 ? context.getDrawable(top) : null;
        if (drawableTop != null) {
            drawableTop.setBounds(0, 0, (int) mDrawableTopWidth, (int) mDrawableTopHeight);
        }
        //设置底部Drawable大小
        Drawable drawableBottom = bottom != 0 ? context.getDrawable(bottom) : null;
        if (drawableBottom != null) {
            drawableBottom.setBounds(0, 0, (int) mDrawableBottomWidth, (int) mDrawableBottomHeight);
        }
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }
}
