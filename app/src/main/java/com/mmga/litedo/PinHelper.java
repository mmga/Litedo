package com.mmga.litedo;

import com.mmga.litedo.db.DBUtil;


public class PinHelper {
    private static int pinNumber = DBUtil.getPinNumber();

    public static int getPinNumber() {
        return pinNumber;
    }

    public static void setPinNumber(int pinNumber) {
        PinHelper.pinNumber = pinNumber;
    }

    public static void plusPinNum() {
        pinNumber++;
    }

    public static void minusPinNum() {
        pinNumber--;
    }
}
