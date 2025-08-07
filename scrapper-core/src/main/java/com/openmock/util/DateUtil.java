package com.openmock.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String dateToStr(Date d, String format) {
        String strDate = "";
        if (d != null) {
            DateFormat dateFormat = new SimpleDateFormat(format);
            strDate = dateFormat.format(d);
        }
        return strDate;
    }

    public static String dateToUTC(Date d) {
        return dateToStr(d, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    /**
     * Generate a Date object from a string in UTC format
     *
     * @param utc - String which contains a date in UTC format, e.g.
     *            "2020-05-21T16:30:52.123Z"
     * @return Date object from a string in UTC format
     * @throws ParseException if the date doesn't math the pattern "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static Date utcToDate(String utc) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(utc);
    }

    public static Date strToDate(String strDate, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(strDate);
    }

    public static Date strEngToDate(String strDate, String format) throws ParseException {
        return new SimpleDateFormat(format, Locale.ENGLISH).parse(strDate);
    }

    public static Date strToDate(String strDate, Locale locale, String format) throws ParseException {
        return new SimpleDateFormat(format, locale).parse(strDate);
    }
}
