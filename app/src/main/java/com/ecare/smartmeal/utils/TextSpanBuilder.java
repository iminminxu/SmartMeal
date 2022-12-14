package com.ecare.smartmeal.utils;

import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * NeighborhoodLongevity
 * <p>
 * Created by xuminmin on 12/2/21.
 * Email: iminminxu@gmail.com
 */
public class TextSpanBuilder {

    private int defaultValue = 0x12000000;
    private CharSequence text;
    private int flag;
    @ColorInt
    private int backgroundColor;
    @ColorInt
    private int foregroundColor;
    @ColorInt
    private int quoteColor;
    private boolean isLeadingMargin;
    private int firstMargin;
    private int restMargin;
    private boolean isBullet;
    private int gapWidth;
    private int bulletColor;
    private float proportion;
    private float xProportion;
    private boolean isStrikeThrough;
    private boolean isUnderline;
    private boolean isSuperscript;
    private boolean isSubscript;
    private boolean isBold;
    private boolean isItalic;
    private boolean isBoldItalic;
    private int size = 0;
    private String fontFamily;
    private Layout.Alignment align;
    private ClickableSpan clickSpan;
    private String url;
    private boolean isBlur;
    private float blurRadius;
    private BlurMaskFilter.Blur blurStyle;
    private SpannableStringBuilder builder;
    private List<Object> spans = new ArrayList<>();

    public static TextSpanBuilder create(@NonNull CharSequence text) {
        return new TextSpanBuilder(text);
    }

    private TextSpanBuilder(@NonNull CharSequence text) {
        this.text = text;
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        foregroundColor = defaultValue;
        backgroundColor = defaultValue;
        quoteColor = defaultValue;
        proportion = -1;
        xProportion = -1;
        builder = new SpannableStringBuilder();
    }

    /**
     * ????????????
     *
     * @param flag <ul>
     *             <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li>
     *             </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder flag(int flag) {
        this.flag = flag;
        return this;
    }

    /**
     * ???????????????
     *
     * @param color ?????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder backgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * ???????????????
     *
     * @param color ?????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder foregroundColor(@ColorInt int color) {
        this.foregroundColor = color;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param color ??????????????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder quoteColor(@ColorInt int color) {
        this.quoteColor = color;
        return this;
    }

    /**
     * ????????????
     *
     * @param first ????????????
     * @param rest  ???????????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder leadingMargin(int first, int rest) {
        this.firstMargin = first;
        this.restMargin = rest;
        isLeadingMargin = true;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param gap   ??????????????????????????????
     * @param color ?????????????????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder bullet(int gap, int color) {
        gapWidth = gap;
        bulletColor = color;
        isBullet = true;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param proportion ??????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder proportion(float proportion) {
        this.proportion = proportion;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param proportion ??????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder xProportion(float proportion) {
        this.xProportion = proportion;
        return this;
    }

    /**
     * ???????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder strikeThrough() {
        this.isStrikeThrough = true;
        return this;
    }

    /**
     * ???????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder underline() {
        this.isUnderline = true;
        return this;
    }

    /**
     * ????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder superscript() {
        this.isSuperscript = true;
        return this;
    }

    /**
     * ????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder subscript() {
        this.isSubscript = true;
        return this;
    }

    /**
     * ????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder bold() {
        isBold = true;
        return this;
    }

    /**
     * ????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder italic() {
        isItalic = true;
        return this;
    }

    /**
     * ???????????????
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder boldItalic() {
        isBoldItalic = true;
        return this;
    }

    /**
     * ????????????
     *
     * @param size ??????????????????px
     */
    public TextSpanBuilder sizeInPx(int size) {
        this.size = size;
        return this;
    }

    /**
     * ????????????
     *
     * @param fontFamily ??????
     *                   <ul>
     *                   <li>monospace</li>
     *                   <li>serif</li>
     *                   <li>sans-serif</li>
     *                   </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder fontFamily(@Nullable String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * ????????????
     *
     * @param align ????????????
     *              <ul>
     *              <li>{@link Layout.Alignment#ALIGN_NORMAL}??????</li>
     *              <li>{@link Layout.Alignment#ALIGN_OPPOSITE}??????</li>
     *              <li>{@link Layout.Alignment#ALIGN_CENTER}??????</li>
     *              </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder align(@Nullable Layout.Alignment align) {
        this.align = align;
        return this;
    }

    /**
     * ??????????????????
     * <p>?????????view.setMovementMethod(LinkMovementMethod.getInstance())</p>
     *
     * @param clickSpan ????????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder click(@NonNull ClickableSpan clickSpan) {
        this.clickSpan = clickSpan;
        return this;
    }

    /**
     * ???????????????
     * <p>?????????view.setMovementMethod(LinkMovementMethod.getInstance())</p>
     *
     * @param url ?????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder url(@NonNull String url) {
        this.url = url;
        return this;
    }

    /**
     * ????????????
     * <p>??????bug?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p>
     * <p>????????????????????????????????????????????????</p>
     *
     * @param radius ????????????????????????0???
     * @param style  ????????????<ul>
     *               <li>{@link  BlurMaskFilter.Blur#NORMAL}</li>
     *               <li>{@link  BlurMaskFilter.Blur#SOLID}</li>
     *               <li>{@link  BlurMaskFilter.Blur#OUTER}</li>
     *               <li>{@link  BlurMaskFilter.Blur#INNER}</li>
     *               </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder blur(float radius, BlurMaskFilter.Blur style) {
        this.blurRadius = radius;
        this.blurStyle = style;
        this.isBlur = true;
        return this;
    }

    public TextSpanBuilder span(Object span) {
        spans.add(span);
        return this;
    }

    /**
     * ?????????????????????
     *
     * @param text ?????????????????????
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder append(@NonNull CharSequence text) {
        buildSpan();
        this.text = text;
        return this;
    }

    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    public SpannableStringBuilder build() {
        buildSpan();
        return builder;
    }

    /**
     * ????????????
     */
    private void buildSpan() {
        int start = builder.length();
        builder.append(text);
        int end = builder.length();
        if (backgroundColor != defaultValue) {
            builder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
            backgroundColor = defaultValue;
        }
        if (foregroundColor != defaultValue) {
            builder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
            foregroundColor = defaultValue;
        }
        if (isLeadingMargin) {
            builder.setSpan(new LeadingMarginSpan.Standard(firstMargin, restMargin), start, end, flag);
            isLeadingMargin = false;
        }
        if (quoteColor != defaultValue) {
            builder.setSpan(new QuoteSpan(quoteColor), start, end, 0);
            quoteColor = defaultValue;
        }
        if (isBullet) {
            builder.setSpan(new BulletSpan(gapWidth, bulletColor), start, end, 0);
            isBullet = false;
        }
        if (proportion != -1) {
            builder.setSpan(new RelativeSizeSpan(proportion), start, end, flag);
            proportion = -1;
        }
        if (xProportion != -1) {
            builder.setSpan(new ScaleXSpan(xProportion), start, end, flag);
            xProportion = -1;
        }
        if (isStrikeThrough) {
            builder.setSpan(new StrikethroughSpan(), start, end, flag);
            isStrikeThrough = false;
        }
        if (isUnderline) {
            builder.setSpan(new UnderlineSpan(), start, end, flag);
            isUnderline = false;
        }
        if (isSuperscript) {
            builder.setSpan(new SuperscriptSpan(), start, end, flag);
            isSuperscript = false;
        }
        if (isSubscript) {
            builder.setSpan(new SubscriptSpan(), start, end, flag);
            isSubscript = false;
        }
        if (isBold) {
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
            isBold = false;
        }
        if (isItalic) {
            builder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
            isItalic = false;
        }
        if (isBoldItalic) {
            builder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, flag);
            isBoldItalic = false;
        }
        if (size > 0) {
            builder.setSpan(new AbsoluteSizeSpan(size), start, end, flag);
            size = 0;
        }
        if (fontFamily != null) {
            builder.setSpan(new TypefaceSpan(fontFamily), start, end, flag);
            fontFamily = null;
        }
        if (align != null) {
            builder.setSpan(new AlignmentSpan.Standard(align), start, end, flag);
            align = null;
        }
        if (clickSpan != null) {
            builder.setSpan(clickSpan, start, end, flag);
            clickSpan = null;
        }
        if (url != null) {
            builder.setSpan(new URLSpan(url), start, end, flag);
            url = null;
        }
        if (isBlur) {
            builder.setSpan(new MaskFilterSpan(new BlurMaskFilter(blurRadius, blurStyle)), start, end, flag);
            isBlur = false;
        }
        if (spans.size() > 0) {
            for (Object span : spans) {
                builder.setSpan(span, start, end, flag);
            }
            spans.clear();
        }
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
    }
}
