package com.heyunxia.utils;

import java.util.Calendar;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-30 下午2:21
 */
public class CounterTest {

    private static final int SUBWAY_PRICE = 5;
    private static final int BUS_PRICE = 1;

    public static void main(String[] args) {

        //计算规则：
        //1　每月一号开始全价０折扣
        //2　累计价格超过100元后，打８折
        //3　累计价格超过150元后，打5折
        //4　累计价格超过400元后，全价０折扣

        count(false);

    }

    /**
     * 计算每天通勤花费
     *
     * @param extra 是否计算公交车 false:不计算，　true:计算
     */
    public static void count(boolean extra) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        System.out.println(month);

        int day = calendar.get(Calendar.DATE);
        System.out.println(day);

        float sum = 0;
        float tempPrice = SUBWAY_PRICE;
        for (int i = 0; i < day; i++) {
            System.out.print(String.format("当前为第%02d天:\t", (i + 1)));
            tempPrice = getTempPrice(sum);
            System.out.print("AM票价: " + tempPrice + "\t");
            sum = sum + tempPrice + getExtra(extra); //am
            System.out.print(String.format("\t花费%4.2f元\t", sum));
            tempPrice = getTempPrice(sum);
            System.out.print("PM票价: " + tempPrice + "\t");
            sum = sum + tempPrice + getExtra(extra); //pm
            System.out.print(String.format("\t花费%4.2f元\t", sum));

            System.out.println();
        }

    }

    private static int getExtra(boolean extra) {
        if (extra) return BUS_PRICE;
        else return 0;
    }

    private static float getTempPrice(float sum) {
        float tempPrice;
        if (sum > 400) {
            tempPrice = SUBWAY_PRICE;
        } else if (sum > 150) {
            tempPrice = SUBWAY_PRICE * 0.5f;
        } else if (sum > 100) {
            tempPrice = SUBWAY_PRICE * 0.8f;
        } else { //小于１００
            tempPrice = SUBWAY_PRICE;
        }
        return tempPrice;
    }
}
