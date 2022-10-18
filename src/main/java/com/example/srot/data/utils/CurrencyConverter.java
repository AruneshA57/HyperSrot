package com.example.srot.data.utils;

public class CurrencyConverter {

    public static String paiseLongToRupeeString(Long paise) {
        StringBuilder rupeeBuilder = new StringBuilder(String.valueOf(paise));
        if(rupeeBuilder.length() > 2)
            rupeeBuilder.insert(rupeeBuilder.length() - 2, '.');
        return rupeeBuilder.toString();
    }
}
