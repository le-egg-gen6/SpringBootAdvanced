package com.myproject.springbootzalopay.utils;

import com.myproject.springbootzalopay.shared.constants.DateConstant;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 2:19 PM Fri 4/18/2025
 */
@UtilityClass
public class DateUtils {

	private static final DateFormat ZALOPAY_DATE_FORMAT = new SimpleDateFormat(DateConstant.DATE_FORMAT);

	private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	private static final Calendar VN_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

	public static String parseZaloPayFormat(Date date) {
		return ZALOPAY_DATE_FORMAT.format(date);
	}

}
