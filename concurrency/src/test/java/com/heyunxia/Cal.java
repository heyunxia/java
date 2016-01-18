package com.heyunxia;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2016-01-18 上午11:46
 */
public class Cal {
    private int i = 1;
    private static int tatalMonth = 180;

    public static void main(String[] args) {
        Cal cal = new Cal();

        calendar.set(Calendar.YEAR, 2013);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        calendar.set(Calendar.DATE, 10);

        double initRate = 0.0655d;
        double initBase = 300000;
        cal.calInit(initRate, initBase, tatalMonth);




    }

    /**
     * 计算本息和:
     * @return
     */
    private double getBaseRate(double base, double rateYear, double month){
        double rateM = rateYear / 12;
        return (base * rateM * Math.pow(1 + rateM, month)) / (Math.pow((1 + rateM), month) - 1);
    }

    public void calInit(double initRate, double initBase, int month) {

        double returnM = getBaseRate(initBase, initRate, month);
        System.out.println("月还款额(本息和):" + returnM);

        double allReturn = returnM * month;
        System.out.println("总还金额：" + allReturn);

        System.out.println("期数     \t时间   \t\t利息  \t\t\t本金   \t\t本息和    \t\t余额");

        calEveryOne(initBase, initRate / 12, returnM);
        /*double rateFirst = base * rateM;
        System.out.println("初期还款利息：" + rateFirst);
        double baseFirst = returnM - rateFirst;
        System.out.println("初期还款本金：" + baseFirst);*/


    }

    private static final String P = "%4.10s";
    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");

    public void calEveryOne(double base, double rateMonth, double returnM) {
        double rateFirst; //利息
        //System.out.println("初期还款利息：" + rateFirst);
        double baseFirst;//本金
        //System.out.println("初期还款本金：" + baseFirst);
        //System.out.println(String.format("%4.18s", 1606.1840466819654));

        //;
        double hasValue;


        if (i== 15) { //初次变动利率　 6.55  vs　6.15;
            //21天 + 9天
            double rateDay = rateMonth / 30; //旧的天利率
            double currentRateYear = 0.0615d; //新年息率
            double currentRateMonth = 0.0615d / 12; //新的月息率
            rateFirst = base * rateDay * 22 +  base * currentRateMonth / 30 * 9;//需还新利息

            double newReturnM = getBaseRate(base, currentRateYear, tatalMonth - 14);
            //System.out.println(newReturnM);

            baseFirst = returnM / 30 * 22 + newReturnM / 30 * 9 - rateFirst;//需还新本金
            hasValue = base - baseFirst;//剩余金额

            returnM = newReturnM;
            rateMonth = currentRateMonth;
        }if (i== 27) { //初次变动利率　 6.15  vs　4.9;
            //21天 + 9天
            double rateDay = rateMonth / 30; //旧的天利率
            double currentRateYear = 0.049d; //新年息率
            double currentRateMonth = currentRateYear / 12; //新的月息率
            rateFirst = base * rateDay * 22 +  base * currentRateMonth / 30 * 9;//需还新利息

            double newReturnM = getBaseRate(base, currentRateYear, tatalMonth - 14);
            //System.out.println(newReturnM);

            baseFirst = returnM / 30 * 22 + newReturnM / 30 * 9 - rateFirst;//需还新本金
            hasValue = base - baseFirst;//剩余金额

            returnM = newReturnM;
            rateMonth = currentRateMonth;
        } else {
            rateFirst = base * rateMonth; //利息
            baseFirst = returnM - rateFirst;//本金
            hasValue = base - baseFirst;//剩余金额
        }

        println(rateFirst, baseFirst, returnM, hasValue, rateMonth * 12);
        //System.out.println((i++) + "\t\t利息：" + String.format(P,rateFirst) + "\t\t本金：" + String.format(P,baseFirst) + "\t\t余额：" + String.format(P,hasValue));
        if (base > 0 && hasValue >0) {
            calEveryOne(hasValue, rateMonth, returnM);
        }
    }


    private void println(double rateFirst, double baseFirst, double returnM, double hasValue, double rate) {
        System.out.println((i++) + "\t\t" + sdf.format(calendar.getTime()) +"\t\t" + String.format(P,rateFirst) + "\t\t" + String.format(P,baseFirst) + "\t\t" + String.format(P,returnM) + "\t\t" + String.format(P,hasValue) + "\t\t" + String.format(P,rate));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
    }
}
