package com.myproject.springbootpayment.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author nguyenle
 * @since 7:24 AM Wed 4/23/2025
 */
@UtilityClass
public class DateUtils {

    private static final String ISO_8601_DATE_FORMAT_STR = "\\d{4}-\\d{2}-\\d{2}";
    private static final String VNPAY_DATE_FORMAT_STR = "yyyyMMddHHmmss";
    private static final String ZALOPAY_DATE_FORMAT_STR = "yyMMdd";

    private static final DateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat(ISO_8601_DATE_FORMAT_STR);
    private static final DateFormat VNPAY_DATE_FORMAT = new SimpleDateFormat(VNPAY_DATE_FORMAT_STR);
    private static final DateFormat ZALOPAY_DATE_FORMAT = new SimpleDateFormat(ZALOPAY_DATE_FORMAT_STR);

    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static final Calendar VN_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

    public static String parseISO8601Date(Date date) {
        return ISO_8601_DATE_FORMAT.format(date);
    }

    public static String parseVNPAYFormat(Date date) {
        return VNPAY_DATE_FORMAT.format(date);
    }

    public static String parseZaloPayFormat(Date date) {
        return ZALOPAY_DATE_FORMAT.format(date);
    }

    public static long getUTCTimestamp() {
        return UTC_CALENDAR.getTimeInMillis();
    }

    public static long getVietnamTimestamp() {
        return VN_CALENDAR.getTimeInMillis();
    }

}
