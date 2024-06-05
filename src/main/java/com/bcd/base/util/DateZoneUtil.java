package com.bcd.base.util;


import com.bcd.base.exception.MyException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * 此类为日期帮助类(专属于某个时区)
 * 方法都参见
 *
 * @see DateUtil
 * <p>
 * 所有的操作方法都基于某个时区
 */
public class DateZoneUtil {
    public final static ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");

    public final static DateTimeFormatter DATE_TIME_FORMATTER_DAY = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_DAY).withZone(ZONE_OFFSET);
    public final static DateTimeFormatter DATE_TIME_FORMATTER_SECOND = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_SECOND).withZone(ZONE_OFFSET);
    public final static DateTimeFormatter DATE_TIME_FORMATTER_MILLISECOND = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_MILLISECOND).withZone(ZONE_OFFSET);

    /**
     * 根据dateStr长度转换成不同的时间
     * {@link DateUtil#DATE_FORMAT_DAY} 长度8
     * {@link DateUtil#DATE_FORMAT_SECOND} 长度14
     *
     * @param dateStr
     * @return
     */
    public static Date stringToDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        int len = dateStr.length();
        switch (len) {
            case 8: {
                return DateZoneUtil.stringToDate_day(dateStr);
            }
            case 14: {
                return DateZoneUtil.stringToDate_second(dateStr);
            }
            case 17: {
                return DateZoneUtil.stringToDate_millisecond(dateStr);
            }
            default: {
                throw MyException.get("dateStr[{}] not support", dateStr);
            }
        }
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date stringToDate_day(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        return Date.from(LocalDate.parse(dateStr, DATE_TIME_FORMATTER_DAY).atTime(LocalTime.MIN).toInstant(ZONE_OFFSET));
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date stringToDate_second(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        return Date.from(LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER_SECOND).toInstant(ZONE_OFFSET));
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date stringToDate_millisecond(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        return Date.from(LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER_MILLISECOND).toInstant(ZONE_OFFSET));
    }

    /**
     * @param date
     * @return
     */
    public static String dateToString_day(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMATTER_DAY.format(date.toInstant());
    }

    /**
     * @param date
     * @return
     */
    public static String dateToString_second(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMATTER_SECOND.format(date.toInstant());
    }

    /**
     * @param date
     * @return
     */
    public static String dateToString_millisecond(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMATTER_MILLISECOND.format(date.toInstant());
    }


    /**
     * @param date
     * @param unit
     * @return
     * @see DateUtil#getFloorDate(Date, ChronoUnit, ZoneOffset)
     */
    public static Date getFloorDate(Date date, ChronoUnit unit) {
        return DateUtil.getFloorDate(date, unit, ZONE_OFFSET);
    }

    /**
     * @param date
     * @param unit
     * @return
     * @see DateUtil#getCeilDate(Date, ChronoUnit, ZoneOffset)
     */
    public static Date getCeilDate(Date date, ChronoUnit unit) {
        return DateUtil.getCeilDate(date, unit, ZONE_OFFSET);
    }

    /**
     * @param startDate
     * @param endDate
     * @param unit
     * @return
     * @see DateUtil#rangeDate(Date, Date, ChronoUnit, ZoneOffset)
     */
    public static List<Date[]> rangeDate(Date startDate, Date endDate, ChronoUnit unit) {
        return DateUtil.rangeDate(startDate, endDate, unit, ZONE_OFFSET);
    }

    /**
     * @param startDate
     * @param endDate
     * @see DateUtil#formatDateParam(Date, Date, ZoneOffset)
     */
    public static void formatDateParam(Date startDate, Date endDate) {
        DateUtil.formatDateParam(startDate, endDate, ZONE_OFFSET);
    }

    public static void main(String[] args) {
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").withZone(ZoneOffset.of("+8"));
        Clock clock = Clock.system(ZONE_OFFSET);
        Instant instant = Instant.now(clock);
        LocalDateTime ldt = LocalDateTime.now();
        OffsetDateTime odt = OffsetDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.now();
        System.out.println(dtf.format(instant));
        System.out.println(ldt.format(dtf));
        System.out.println(odt.format(dtf));
        System.out.println(zdt.format(dtf));
        System.out.println(Clock.system(ZONE_OFFSET));
        System.out.println(Clock.systemUTC());
    }
}
