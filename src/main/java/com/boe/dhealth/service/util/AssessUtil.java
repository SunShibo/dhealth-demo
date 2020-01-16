package com.boe.dhealth.service.util;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service("assessService")
@Transactional
public class AssessUtil {

    /**
     * -左腿
     *
     * @param leftHipX   左髋
     * @param leftHipY
     * @param leftKneeX  左膝
     * @param leftKneeY
     * @param leftKneeX2 左膝
     * @param leftKneeY2
     * @param leftAnkleX 左脚踝
     * @param leftAnkleY
     * @return
     */
    public static BigDecimal getO_leftleg_R(int leftHipX, int leftHipY, int leftKneeX, int leftKneeY, int leftKneeX2, int leftKneeY2, int leftAnkleX, int leftAnkleY, int threshold_O_l) {
        double line1 = MathUtil.getAngle(leftHipX, leftHipY, leftKneeX, leftKneeY);
        double line2 = MathUtil.getAngle(leftKneeX2, leftKneeY2, leftAnkleX, leftAnkleY);
        //左腿正数为X型腿 负数为O型腿
        double angle = 180 - line1 - (180 - line2);
        // 两条直线夹角  angle = angle-2;
        BigDecimal angleResult = new BigDecimal(angle).divide(new BigDecimal(threshold_O_l), 4, BigDecimal.ROUND_HALF_UP);
        return angleResult.multiply(new BigDecimal(100));
    }

    /**
     * -右腿
     *
     * @param rightHipX   右髋
     * @param rightHipY
     * @param rightKneeX  右膝
     * @param rightKneeY
     * @param rightKneeX2 右膝
     * @param rightKneeY2
     * @param rightAnkleX 右脚踝
     * @param rightAnkleY
     * @return
     */
    public static BigDecimal getO_rightleg_R(int rightHipX, int rightHipY, int rightKneeX, int rightKneeY, int rightKneeX2, int rightKneeY2, int rightAnkleX, int rightAnkleY, int threshold_O_r) {
        double line1 = MathUtil.getAngle(rightHipX, rightHipY, rightKneeX, rightKneeY);
        double line2 = MathUtil.getAngle(rightKneeX2, rightKneeY2, rightAnkleX, rightAnkleY);
        double angle = 180 - line1 - (180 - line2);
        //右腿负数为X型腿 正数为O型腿
        //  右腿两条直线夹角  angle = angle+2.5;
        BigDecimal angleResult = new BigDecimal(angle).divide(new BigDecimal(threshold_O_r), 4, BigDecimal.ROUND_HALF_UP);
        return angleResult.multiply(new BigDecimal(100));
    }


    /**
     * 头部侧倾-左右耳水平度
     *
     * @param leftEarX  左耳
     * @param leftEarY
     * @param rightEarX 右耳
     * @param rightEarY
     * @return
     */
    public static BigDecimal getHeadHeel_R(int leftEarX, int leftEarY, int rightEarX, int rightEarY, int threshold_head) {
        double K = MathUtil.getAngle(leftEarX, leftEarY, rightEarX, rightEarY);
        //角度小于0为左倾
        BigDecimal R = new BigDecimal(K).divide(new BigDecimal(threshold_head), 4, BigDecimal.ROUND_HALF_UP);
        //角度小于0为左倾，大于0为右倾
        return R.multiply(new BigDecimal(100));
    }

    /**
     * 高低肩-左右肩水平度
     *
     * @param leftShoulderX  左肩
     * @param leftShoulderY
     * @param rightShoulderX 右肩
     * @param rightShoulderY
     * @return
     */
    public static BigDecimal getHighLowShoulder(int leftShoulderX, int leftShoulderY, int rightShoulderX, int rightShoulderY, int threshold_shoulder) {
        double K = MathUtil.getAngle(leftShoulderX, leftShoulderY, rightShoulderX, rightShoulderY);
        //高低肩-左右肩水平度"+K
        BigDecimal R = new BigDecimal(K).divide(new BigDecimal(threshold_shoulder), 4, BigDecimal.ROUND_HALF_UP);
        return R.multiply(new BigDecimal(100));
    }

    /**
     * 盆骨侧倾 - 左右髋部水平度
     *
     * @param leftHipX  左髋
     * @param leftHipY
     * @param rightHipX 右髋
     * @param rightHipY
     * @return
     */
    public static BigDecimal getPelvicTilt_R(int leftHipX, int leftHipY, int rightHipX, int rightHipY, int threshold_hip) {
        double K = MathUtil.getAngle(leftHipX, leftHipY, rightHipX, rightHipY);
        //盆骨侧倾 - 左右髋部水平度" + K
        BigDecimal R = new BigDecimal(K).divide(new BigDecimal(threshold_hip), 4, BigDecimal.ROUND_HALF_UP);
        return R.multiply(new BigDecimal(100));
    }

    /**
     * 脊柱侧弯 - 水平肩-水平髋的角度
     */
    public static BigDecimal getScoliosis(int leftShoulderX, int leftShoulderY, int rightShoulderX, int rightShoulderY, int leftHipX, int leftHipY, int rightHipX, int rightHipY, int threshold_spine) {
        double shoulder = MathUtil.getAngle(leftShoulderX, leftShoulderY, rightShoulderX, rightShoulderY);
        double hip = MathUtil.getAngle(leftHipX, leftHipY, rightHipX, rightHipY);
        double angle = 0;
        if (shoulder > 0) {
            if (hip > 0) {
                angle = shoulder - hip;
            }
            if (hip < 0) {
                angle = shoulder - hip;
            }
            if (hip == 0) {
                angle = shoulder;
            }
        }
        if (shoulder < 0) {
            if (hip == 0) {
                angle = shoulder;
            } else {
                angle = shoulder - hip;
            }

        }
        if (shoulder == 0) {
            angle = shoulder - hip;
        }
        //脊柱侧弯 - 双肩与双髋的角度" + angle
        BigDecimal angleResult = new BigDecimal(angle).divide(new BigDecimal(threshold_spine), 4, BigDecimal.ROUND_HALF_UP);
        return angleResult.multiply(new BigDecimal(100));
    }


    public static double getR(double S, double K, double A) {
        double r = 0;
        if (Double.doubleToLongBits(K) <= Double.doubleToLongBits(A)) {
            r = 60 * K / A;
        } else if (Double.doubleToLongBits(A) < Double.doubleToLongBits(K) &&
                Double.doubleToLongBits(K) <= Double.doubleToLongBits(2 * A)) {
            r = 20 * ((K - A) / A) + 60;
        } else if (Double.doubleToLongBits(K) > Double.doubleToLongBits(2 * A)) {
            double sigmoid = S * (K - 2 * A);
            double t = MathUtil.Sigmoid(sigmoid);
            r = 20 * t + 80;
        }
        return r;

    }
    public static double getRandom() {
        Random random = new Random();
        double num = random.nextDouble();
        return num / 10;
    }


    /**
     * 盆骨前倾&头前倾
     */
    public static double hepOs(double c,int num){
        BigDecimal R = new BigDecimal(c).divide(new BigDecimal(num), 4, BigDecimal.ROUND_HALF_UP);
        return R.multiply(new BigDecimal(100)).doubleValue();
    }

}