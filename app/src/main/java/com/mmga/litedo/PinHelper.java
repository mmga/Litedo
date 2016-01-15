package com.mmga.litedo;

import com.mmga.litedo.db.DBUtil;

/**
 * Created by mmga on 2016/1/15.
 */
public  class PinHelper {
    private static int pinNumber = DBUtil.getPinNumber();

    public static int getPinNumber() {
        return pinNumber;
    }

    public static void setPinNumber(int pinNumber) {
        PinHelper.pinNumber = pinNumber;
    }
}
