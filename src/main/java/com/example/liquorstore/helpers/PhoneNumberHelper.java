package com.example.liquorstore.helpers;

public class PhoneNumberHelper {
    public String normalizeMobileNumber(String mobileNumber) {
        if (mobileNumber.startsWith("0")) {

            return "254" + mobileNumber.substring(1);
        } else if (mobileNumber.startsWith("+254")) {
            return mobileNumber.substring(1);
        } else if (mobileNumber.startsWith("1")) {
            return "254" + mobileNumber;
        } else if (mobileNumber.startsWith("7")) {
            return "254" + mobileNumber;
        }
        return mobileNumber;
    }
}
