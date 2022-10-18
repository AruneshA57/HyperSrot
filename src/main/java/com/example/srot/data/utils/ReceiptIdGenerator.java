package com.example.srot.data.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ReceiptIdGenerator {

    /*private static Date date = new Date(2021, Calendar.DECEMBER, 27);
    private static Long count = 1L;

    private static boolean compareDatesHaveSameDay(Date d1, Date d2) {
        //Copied from StackOverflow
        // (stackoverflow.com/questions/2517709/comparing-two-java-util-dates-to-see-if-they-are-in-the-same-day)
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }*/

    public static String getReceiptId() {
        /*Date currentDate = new Date();
        if(!compareDatesHaveSameDay(date, currentDate)) {
            date = currentDate;
            count = 1L;
        }
        else {
            ++count;
        }

        return Long.toString(date.getTime()) + Long.toString(count);*/

        return UUID.randomUUID().toString().replace("-", "");
    }
}
