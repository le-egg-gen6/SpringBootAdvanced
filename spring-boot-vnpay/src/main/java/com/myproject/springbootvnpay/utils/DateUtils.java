package com.myproject.springbootvnpay.utils;

import com.myproject.springbootvnpay.shared.constant.DateConstant;
import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author nguyenle
 * @since 11:05 AM Mon 3/10/2025
 */
@UtilityClass
public class DateUtils {
    private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat(DateConstant.ISO_8601_DATE_FORMAT);
    private static final DateFormat VNPAY_DATE_FORMAT = new SimpleDateFormat(DateConstant.VNPAY_DATETIME_FORMAT);

    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static final Calendar VN_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

    public static String parseISOFormat(Date date) {
        return ISO_DATE_FORMAT.format(date);
    }

    public static String parseVNPAYFormat(Date date) {
        return VNPAY_DATE_FORMAT.format(date);
    }

    public static Date getUTCTime() {
        return UTC_CALENDAR.getTime();
    }

    public static Date getVNTime() {
        return VN_CALENDAR.getTime();
    }

    public static Date getVNTimeAfter(TimeUnit timeUnit, int amount) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        calendar.add(Calendar.MILLISECOND, (int) timeUnit.toMillis(amount));
        return calendar.getTime();
    }

}
