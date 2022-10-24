package com.ecare.smartmeal.utils;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.TimeUtils;

/**
 * NeighborhoodLongevity
 * <p>
 * Created by xuminmin on 12/21/21.
 * Email: iminminxu@gmail.com
 */
public class IDCardUtils {

    /**
     * 通过身份证号获取性别
     *
     * @param iDCard 身份证号
     * @return 0男1女
     */
    public static int getGenderByIDCard(String iDCard) {
        if (!RegexUtils.isIDCard18(iDCard)) {
            return 0;
        }
        return NumUtils.parseInt(iDCard.substring(16).substring(0, 1)) % 2 == 0 ? 1 : 0;
    }

    /**
     * 通过身份证号获取年龄
     *
     * @param iDCard 身份证号
     * @return 年龄
     */
    public static int getAgeByIDCard(String iDCard) {
        if (!RegexUtils.isIDCard18(iDCard)) {
            return 0;
        }
        return NumUtils.parseInt(TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy"))) - NumUtils.parseInt(iDCard.substring(6).substring(0, 4));
    }

    /**
     * 通过身份证号获取出生年月
     *
     * @param iDCard 身份证号
     * @return 出生年月
     */
    public static String getDateOfBirthByIDCard(String iDCard) {
        if (!RegexUtils.isIDCard18(iDCard)) {
            return "";
        }
        return iDCard.substring(6).substring(0, 4) + "年" + iDCard.substring(10).substring(0, 2) + "月" + iDCard.substring(12).substring(0, 2) + "日";
    }
}
