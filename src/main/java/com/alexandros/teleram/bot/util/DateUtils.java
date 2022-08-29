package com.alexandros.teleram.bot.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class DateUtils {

    public static final String RESERVATION_TIMESTAMP_PATTERN = "dd/MM/yyyy hh:mm";
    private static final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public static Date parseDate(String dateStr){
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try{
            SimpleDateFormat dtFormat = new SimpleDateFormat(RESERVATION_TIMESTAMP_PATTERN);
            return dtFormat.parse(dateStr);
        }catch (ParseException e){
            logger.error("Parse exception caught while parsing date",e);
            return null;
        }
    }

    public static String formatDate(Date date){
        if (Objects.isNull(date)) {
            return null;
        }
        try{
            SimpleDateFormat dtFormat = new SimpleDateFormat(RESERVATION_TIMESTAMP_PATTERN);
            return dtFormat.format(date);
        }catch (Exception e){
            logger.error("Exception caught while formatting date as string",e);
            return null;
        }
    }

    public static Date buildEndOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date buildTomorrow(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    public static Date[] buildTimeRangeDates(Date inputDate, int timeRangeInMin){
        Date[] timeRangeDates = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.set(Calendar.MINUTE, timeRangeInMin);
        timeRangeDates[0] = calendar.getTime();
        calendar.set(Calendar.MINUTE, -(2*timeRangeInMin));
        timeRangeDates[1] = calendar.getTime();
        return timeRangeDates;
    }


}
