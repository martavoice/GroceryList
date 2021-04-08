package com.test.grocerylist.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final SimpleDateFormat dateFormat  = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.US);
    private static DateUtils instance;

    private DateUtils() {
    }

    public static DateUtils getInstance() {
        if (instance == null) {
            instance = new DateUtils();
        }
        return instance;
    }

    public String convertDateToString(long date) {
        Date createdDate = new Date(date);
        return dateFormat.format(createdDate);
    }
}
