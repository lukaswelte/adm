package com.adm.meetup.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 21.1.14.
 */
public class DateHelper {
    public static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static String format(Date date) {
        return dateFormat.format(date);
    }

    public static Date parse(String date) throws ParseException {
        return dateFormat.parse(date);
    }
}
