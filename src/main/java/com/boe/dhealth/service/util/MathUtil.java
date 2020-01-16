package com.boe.dhealth.service.util;

import com.boe.dhealth.domain.KBBO;

import java.awt.*;
import java.math.BigDecimal;


public class MathUtil {


    //通过两点坐标获取直线与X轴角度
    public static double getAngle(int x1, int y1, int x2, int y2) {

        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        double theta = angle * (180 / Math.PI);
        if (theta < 0) {
            theta = -(theta + 180);
        } else {
            theta = 180 - theta;
        }
        return theta;

    }

    //通过两点坐标获取直线与Y轴角度
    public static double getAngleY(int x1, int y1, int x2, int y2) {

        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        double angle = Math.atan2((p2.x - p1.x), (p2.y - p1.y));
        double theta = angle * (180 / Math.PI);
        return theta;

    }

    //通过两个点坐标求直线方程k、b
    public static KBBO getkx(int x1, int y1, int x2, int y2) {
        KBBO kb = new KBBO();
        double k;
        double b;
        if (x2 - x1 != 0) {
            k = new BigDecimal((float) (y2 - y1) / (x2 - x1)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double t = k * x1;
            b = new BigDecimal((x2 * y1 - x1 * y2) / (x2 - x1)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            kb.setK(k);
            kb.setB(b);
            return kb;
        } else {
            k = 0;
            b = y1 = y2;
            kb.setK(k);
            kb.setB(b);
            return kb;
        }

    }

    //通过两个已知点和第三个点的x坐标求第三个点的Y坐标值
    public static double getY(int x1, int y1, int x2, int y2, int x) {
        KBBO testBO = getkx(x1, y1, x2, y2);
        double y = new BigDecimal(testBO.getK() * x + testBO.getB()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return y;
    }

    //通过两点坐标求直线斜率
    public static double getK(int x1, int y1, int x2, int y2) {
        double k = 0;
        //斜率存在
        if (x2 - x1 != 0) {
            k = (y2 - y1) / (x2 - x1);
        }
        if (x2 - x1 == 0) {
            k = 1;
        }
        if (y2 - y1 == 0) {
            k = 0;
        }
        return k;
    }


    //通过四个点坐标获取两直线转向角-左腿的K
    public static double getOLeftLegK(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        double k = 0;
        double k1 = getK(x1, y1, x2, y2);
        double k2 = getK(x3, y3, x4, y4);
        if (k1 * k2 == -1) {
            k = 90;
            return k;
        }
        //k1、k2斜率都存在
        if (k1 != 0 && k2 != 0) {
            double k3 = k2 - k1;
            if (k3 > 0) {
                double tan = (k2 - k1) / (1 + (k1 * k2));
                double angle = Math.toDegrees(Math.atan(tan));
                k = angle;
                return k;
            } else {
                double tan = (k1 - k2) / (1 + (k1 * k2));
                double angle = Math.toDegrees(Math.atan(tan));
                k = angle;
                return k;
            }
        }

        return k;
    }

    //通过四个点坐标获取两直线转向角-左腿的K
    public static double getORightLegK(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        double k = 0;
        double k1 = getK(x1, y1, x2, y2);
        double k2 = getK(x3, y3, x4, y4);
        //k1、k2斜率都存在
        if (k1 != 0 && k2 != 0) {
            double tan = (k2 - k1) / (1 + (k1 * k2));
            double angle = Math.toDegrees(Math.atan((tan < 0) ? -tan : tan));
            k = (k2 > k1) ? angle : -angle;
            return k;
        }
        //K1斜率不存在
        if (k1 == 0) {
            k = getAngle(x3, y3, x4, y4);
            return k;
        }
        //K2斜率不存在
        if (k2 == 0) {
            k = getAngle(x1, y1, x2, y2);
            return -k;
        }
        return k;
    }


    public static double Sigmoid(double K) {
        double sigmoid = 1 / (1 + Math.pow(Math.E, -K));
        return sigmoid;
    }

    //通过两点求两点之间的距离-取绝对值
    public static double getDistance(int x1, int y1, int x2, int y2) {
        double x = Math.pow((x1 - x2), 2);
        double y = Math.pow((y1 - y2), 2);
        double distance = Math.sqrt(x + y);
        return (distance < 0) ? -distance : distance;
    }

    //获取脊柱侧弯的K=Max(L1,L2)/Min(L1,L2)
    public static double getScoliosisK(double l1, double l2) {
        double k;
        if (l1 > l2) {
            k = l1 / l2;
        } else {
            k = l2 / l1;
        }
        return k;
    }

    /**
     * （1）四舍五入把double转化int整型，0.5进一，小于0.5不进一
     *
     * @param number
     * @return
     */
    public static int getInt(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

}
