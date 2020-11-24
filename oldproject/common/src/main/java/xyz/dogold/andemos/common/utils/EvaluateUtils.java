package xyz.dogold.andemos.common.utils;

/**
 * SizeUtils
 * Created by glorin on 24/04/2017.
 */

public class EvaluateUtils {
    public static float evaluateFloat(float start, float end, float fraction) {
        return start + (end - start) * fraction;
    }

    public static int evaluateColor(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    public static float between(float floor, float ceil, float value) {
        return Math.max(floor, Math.min(value, ceil));
    }
}
