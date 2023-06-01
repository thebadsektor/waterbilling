package com.westframework.waterbillingapp.data.helpers;

public class Generator {

    public static String generateOrNumber(String a, String b, int c){
        if(c > 999){
            c = 0;
        }
        String d = String.format("%03d", c);
        String orNum = a.trim() + "-" + b.trim() + "-" + d;
        return orNum;
    }
}
