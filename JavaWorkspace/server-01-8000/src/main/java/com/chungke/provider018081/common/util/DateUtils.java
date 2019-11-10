/*     */ package com.chungke.provider018081.common.util;
/*     */ 
/*     */ import org.springframework.util.StringUtils;

import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
///*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateUtils
/*     */ {
/*  23 */   public static int currentSeconds() { return (int)(System.currentTimeMillis() / 1000L); }
/*     */ 
/*     */   
/*     */   public static Date add(Date dateFrom, int field, int amount) {
/*  27 */     Calendar cd = Calendar.getInstance();
/*  28 */     cd.setTime(dateFrom);
/*  29 */     cd.add(field, amount);
/*  30 */     return cd.getTime();
/*     */   }
/*     */ 
/*     */   
/*  34 */   public static String addDays(String date, int days) { return addDays(date, days, "yyyy-MM-dd"); }
/*     */ 
/*     */   
/*     */   public static String addDays(String date, int days, String format) {
/*     */     try {
/*  39 */       Calendar cal = Calendar.getInstance();
/*  40 */       cal.setTime(parse(date, format));
/*  41 */       cal.add(5, days);
/*  42 */       return format(cal.getTime(), format);
/*  43 */     } catch (Exception e) {
/*  44 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getDayStartTime() {
/*  53 */     Calendar todayStart = Calendar.getInstance();
/*     */     
/*  55 */     todayStart.set(11, 0);
/*     */     
/*  57 */     todayStart.set(12, 0);
/*     */     
/*  59 */     todayStart.set(13, 0);
/*     */     
/*  61 */     todayStart.set(14, 0);
/*     */     
/*  63 */     return todayStart.getTime().getTime() / 1000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date changeTheHourMinuteSecond(Date date1, Date date2) {
/*  74 */     Calendar cl1 = Calendar.getInstance();
/*  75 */     cl1.setTime(date1);
/*  76 */     cl1.set(11, 0);
/*  77 */     cl1.set(12, 0);
/*  78 */     cl1.set(13, 0);
/*  79 */     Calendar cl2 = Calendar.getInstance();
/*  80 */     cl2.setTime(date2);
/*  81 */     cl1.set(11, cl2.get(11));
/*  82 */     cl1.set(12, cl2.get(12));
/*  83 */     cl1.set(13, cl2.get(13));
/*  84 */     return cl1.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long dateDiff(Date start, Date end) {
/*  95 */     long inter = end.getTime() - start.getTime();
/*  96 */     if (inter < 0L) {
/*  97 */       return 0L;
/*     */     }
/*  99 */     long dateMillSec = 86400000L;
/* 100 */     long dateCnt = inter / dateMillSec;
/* 101 */     long remainder = inter % dateMillSec;
/* 102 */     if (remainder != 0L) {
/* 103 */       dateCnt++;
/*     */     }
/* 105 */     return dateCnt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String formatSec(int timesec) {
/* 110 */     long time = timesec * 1000L;
/* 111 */     return format(new Date(time));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static String formatMs(long time) { return format(new Date(time)); }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static String format(Date date) { return format(date, "yyyy-MM-dd HH:mm:ss"); }
/*     */ 
/*     */   
/*     */   public static String format(Date date, String format) {
/*     */     try {
/* 125 */       if (format != null && !"".equals(format) && date != null) {
/* 126 */         SimpleDateFormat formatter = new SimpleDateFormat(format);
/* 127 */         return formatter.format(date);
/*     */       } 
/* 129 */     } catch (Exception e) {}
/*     */     
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatDate(Date d) {
/* 141 */     if (d == null) {
/* 142 */       return null;
/*     */     }
/*     */     try {
/* 145 */       return (new SimpleDateFormat("yyyy-MM-dd")).format(d);
/* 146 */     } catch (Exception e) {
/* 147 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String formatDateToDetail(Date d) {
/* 152 */     if (d == null) {
/* 153 */       return null;
/*     */     }
/*     */     try {
/* 156 */       return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(d);
/* 157 */     } catch (Exception e) {
/* 158 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String formatDateYyyyMmDd(Date date) {
/*     */     try {
/* 164 */       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
/* 165 */       return formatter.format(date);
/* 166 */     } catch (Exception e) {
/* 167 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getCurrentWeekMonday() {
/* 178 */     Calendar cal = Calendar.getInstance();
/* 179 */     cal.set(11, 0);
/* 180 */     cal.set(12, 0);
/* 181 */     cal.set(13, 0);
/* 182 */     cal.set(14, 0);
/* 183 */     int index = cal.get(7);
/*     */     
/* 185 */     if (index == 1) {
/* 186 */       index = 8;
/*     */     }
/* 188 */     cal.add(5, -(index - 2));
/* 189 */     return cal.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getCurrentWeekSaturday() {
/* 199 */     Calendar cal = Calendar.getInstance();
/* 200 */     cal.set(11, 0);
/* 201 */     cal.set(12, 0);
/* 202 */     cal.set(13, 0);
/* 203 */     cal.set(14, 0);
/* 204 */     int index = cal.get(7);
/* 205 */     if (index == 1) {
/* 206 */       index = 8;
/*     */     }
/* 208 */     cal.add(5, -(index - 2));
/* 209 */     cal.add(5, 6);
/* 210 */     return cal.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getDateOfMonthByNum(String month, int num) {
/* 221 */     Calendar cl = Calendar.getInstance();
/* 222 */     cl.setTime(parseDateyyyyMM(month));
/* 223 */     cl.set(11, 0);
/* 224 */     cl.set(12, 0);
/* 225 */     cl.set(13, 0);
/* 226 */     cl.set(14, 0);
/* 227 */     cl.add(6, num - 1);
/* 228 */     return cl.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public static String getDateyyyyMM(Date date) { return (new SimpleDateFormat("yyyyMM")).format(date); }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public static String getDateyyyyMMdd(long date) { return (new SimpleDateFormat("yyyy-MM-d")).format(Long.valueOf(date)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 252 */   public static String getDateyyyyMMdd(Date date) { return (new SimpleDateFormat("yyyyMMdd")).format(date); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDayBetweenDateStartOrEnd(Date dateFrom, Date dateTo, Date month, String flag) {
/* 265 */     if (dateFrom.getTime() > dateTo.getTime()) {
/* 266 */       Date temp = dateFrom;
/* 267 */       dateFrom = dateTo;
/* 268 */       dateTo = temp;
/*     */     } 
/* 270 */     if (getDateyyyyMM(month).compareTo(getDateyyyyMM(dateFrom)) > 0 && getDateyyyyMM(month).compareTo(getDateyyyyMM(dateTo)) < 0)
/* 271 */       return "start".equals(flag) ? 1 : getMonthsMaxDay(month); 
/* 272 */     if (getDateyyyyMM(month).compareTo(getDateyyyyMM(dateFrom)) == 0 && getDateyyyyMM(month).compareTo(getDateyyyyMM(dateTo)) < 0)
/* 273 */       return "start".equals(flag) ? getDayOfMonth(dateFrom) : getMonthsMaxDay(month); 
/* 274 */     if (getDateyyyyMM(month).compareTo(getDateyyyyMM(dateFrom)) > 0 && getDateyyyyMM(month).compareTo(getDateyyyyMM(dateTo)) == 0) {
/* 275 */       return "start".equals(flag) ? 1 : getDayOfMonth(dateTo);
/*     */     }
/* 277 */     return "start".equals(flag) ? getDayOfMonth(dateFrom) : getDayOfMonth(dateTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDayOfMonth(Date date) {
/* 288 */     Calendar cal1 = Calendar.getInstance();
/* 289 */     cal1.setTime(date);
/* 290 */     return cal1.get(5);
/*     */   }
/*     */   
/*     */   public static int getDayofWeek(Date date, int day) {
/* 294 */     Calendar calendar = Calendar.getInstance();
/* 295 */     calendar.setTime(date);
/* 296 */     calendar.set(11, 0);
/* 297 */     calendar.set(12, 0);
/* 298 */     calendar.set(13, 0);
/* 299 */     calendar.set(14, 0);
/* 300 */     calendar.set(5, calendar.get(5) + day - 1);
/* 301 */     return calendar.get(7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDaysBetweenTwoDates(String dateFrom, String dateEnd) {
/* 311 */     Date dtFrom = null;
/* 312 */     Date dtEnd = null;
/* 313 */     dtFrom = string2Date(dateFrom, "yyyy-MM-dd");
/* 314 */     dtEnd = string2Date(dateEnd, "yyyy-MM-dd");
/* 315 */     long begin = dtFrom.getTime();
/* 316 */     long end = dtEnd.getTime();
/* 317 */     long inter = end - begin;
/* 318 */     if (inter < 0L) {
/* 319 */       inter *= -1L;
/*     */     }
/* 321 */     long dateMillSec = 86400000L;
/* 322 */     long dateCnt = inter / dateMillSec;
/* 323 */     long remainder = inter % dateMillSec;
/* 324 */     if (remainder != 0L) {
/* 325 */       dateCnt++;
/*     */     }
/* 327 */     return String.valueOf(dateCnt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getDaysBetweenTwoDatesMath(String dateFrom, String dateEnd) {
/* 337 */     Date dtFrom = null;
/* 338 */     Date dtEnd = null;
/* 339 */     dtFrom = string2Date(dateFrom, "yyyy-MM-dd");
/* 340 */     dtEnd = string2Date(dateEnd, "yyyy-MM-dd");
/* 341 */     long begin = dtFrom.getTime();
/* 342 */     long end = dtEnd.getTime();
/* 343 */     long inter = end - begin;
/* 344 */     long dateMillSec = 86400000L;
/* 345 */     long dateCnt = inter / dateMillSec;
/* 346 */     long remainder = inter % dateMillSec;
/* 347 */     if (remainder != 0L) {
/* 348 */       dateCnt++;
/*     */     }
/* 350 */     return dateCnt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getMaxmizeDate(Date date1, Date date2) {
/* 362 */     if (date1 == null)
/* 363 */       return date2; 
/* 364 */     if (date2 == null)
/* 365 */       return date1; 
/* 366 */     if (date1.compareTo(date2) < 0) {
/* 367 */       return date2;
/*     */     }
/* 369 */     return date1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getMinimizeDate(Date date1, Date date2) {
/* 381 */     if (date1 == null)
/* 382 */       return date2; 
/* 383 */     if (date2 == null)
/* 384 */       return date1; 
/* 385 */     if (date1.compareTo(date2) > 0) {
/* 386 */       return date2;
/*     */     }
/* 388 */     return date1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getMonthBetweenTwoDate(Date fromDate, Date toDate) {
/* 399 */     long begin = fromDate.getTime();
/* 400 */     long end = toDate.getTime();
/* 401 */     long inter = end - begin;
/* 402 */     if (inter < 0L) {
/* 403 */       inter *= -1L;
/*     */     }
/* 405 */     long dateMillSec = 86400000L;
/* 406 */     long dateCnt = inter / dateMillSec;
/* 407 */     long remainder = inter % dateMillSec;
/* 408 */     if (remainder != 0L) {
/* 409 */       dateCnt++;
/*     */     }
/* 411 */     Set<String> set = new LinkedHashSet<>();
/* 412 */     Calendar cl = Calendar.getInstance();
/* 413 */     cl.setTime(fromDate);
/* 414 */     cl.set(11, 0);
/* 415 */     cl.set(12, 0);
/* 416 */     cl.set(13, 0);
/* 417 */     cl.set(14, 0);
/* 418 */     set.add(getDateyyyyMM(cl.getTime()));
/* 419 */     for (int i = 1; i <= dateCnt; i++) {
/* 420 */       cl.add(6, 1);
/* 421 */       set.add(getDateyyyyMM(cl.getTime()));
/*     */     } 
/* 423 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getMonthsFirstDay(Date date) {
/* 434 */     Calendar cal = Calendar.getInstance();
/* 435 */     cal.setTime(date);
/* 436 */     cal.set(5, 1);
/* 437 */     cal.set(11, 0);
/* 438 */     cal.set(12, 0);
/* 439 */     cal.set(13, 0);
/* 440 */     cal.set(14, 0);
/* 441 */     return cal.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getMonthsLastDay(Date date) {
/* 452 */     Calendar cal = Calendar.getInstance();
/* 453 */     cal.setTime(date);
/* 454 */     cal.set(5, 1);
/* 455 */     cal.set(11, 0);
/* 456 */     cal.set(12, 0);
/* 457 */     cal.set(13, 0);
/* 458 */     cal.set(14, 0);
/* 459 */     cal.add(2, 1);
/* 460 */     cal.add(5, -1);
/* 461 */     return cal.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMonthsMaxDay(Date date) {
/* 471 */     Calendar cal1 = Calendar.getInstance();
/* 472 */     cal1.setTime(date);
/* 473 */     return cal1.getActualMaximum(5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getMoveMonthDate(int month) {
/* 483 */     Calendar cl = Calendar.getInstance();
/*     */     
/* 485 */     Date nowDate = new Date();
/* 486 */     cl.set(nowDate.getYear() + 1900, nowDate.getMonth(), nowDate.getDate(), 0, 0, 0);
/* 487 */     cl.add(2, month);
/* 488 */     return cl.getTime();
/*     */   }
/*     */   
/*     */   public static int getWeekByDate(Date date) {
/* 492 */     Calendar cl1 = Calendar.getInstance();
/* 493 */     cl1.setTime(date);
/* 494 */     cl1.set(11, 0);
/* 495 */     cl1.set(12, 0);
/* 496 */     cl1.set(13, 0);
/* 497 */     return cl1.get(7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date getYesterday() {
/* 504 */     Calendar cal = Calendar.getInstance();
/* 505 */     cal.set(11, 0);
/* 506 */     cal.set(12, 0);
/* 507 */     cal.set(13, 0);
/* 508 */     cal.set(14, 0);
/* 509 */     cal.add(5, -1);
/* 510 */     return cal.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDawn(Date date) throws Exception {
/* 518 */     boolean isDawn = false;
/* 519 */     Calendar tempCal = Calendar.getInstance();
/* 520 */     if (date != null) {
/* 521 */       tempCal.setTime(date);
/*     */     }
/* 523 */     int hour = tempCal.get(11);
/* 524 */     if (0 <= hour && hour <= 6) {
/* 525 */       isDawn = true;
/*     */     }
/* 527 */     return isDawn;
/*     */   }
/*     */   
/*     */   public static List<String> listDates(String startdate, String enddate, String format) {
/* 531 */     List<String> dates = new LinkedList<>();
/* 532 */     SimpleDateFormat sformat = new SimpleDateFormat(format);
/*     */     try {
/* 534 */       Date date1 = sformat.parse(startdate);
/* 535 */       Date date2 = sformat.parse(enddate);
/* 536 */       Calendar start = Calendar.getInstance();
/* 537 */       start.setTime(date1);
/* 538 */       Calendar end = Calendar.getInstance();
/* 539 */       end.setTime(date2);
/* 540 */       while (start.compareTo(end) <= 0) {
/* 541 */         String tmp = sformat.format(start.getTime());
/* 542 */         dates.add(tmp);
/* 543 */         start.set(5, start.get(5) + 1);
/*     */       } 
/* 545 */       return dates;
/* 546 */     } catch (ParseException e) {
/* 547 */       e.printStackTrace();
/*     */       
/* 549 */       return null;
/*     */     } 
/*     */   }
/*     */   public static List<Date> listDates(Date date1, Date date2) {
/* 553 */     List<Date> dates = new LinkedList<>();
/* 554 */     Calendar start = Calendar.getInstance();
/* 555 */     start.setTime(date1);
/* 556 */     Calendar end = Calendar.getInstance();
/* 557 */     end.setTime(date2);
/* 558 */     while (start.compareTo(end) <= 0) {
/*     */       
/* 560 */       dates.add(start.getTime());
/* 561 */       start.set(5, start.get(5) + 1);
/*     */     } 
/* 563 */     return dates;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<String> listMonth(String startdate, String enddate, String format) {
/* 568 */     List<String> dates = new LinkedList<>();
/* 569 */     SimpleDateFormat sformat = new SimpleDateFormat(format);
/*     */     try {
/* 571 */       Date date1 = sformat.parse(startdate);
/* 572 */       Date date2 = sformat.parse(enddate);
/* 573 */       Calendar start = Calendar.getInstance();
/* 574 */       start.setTime(date1);
/* 575 */       Calendar end = Calendar.getInstance();
/* 576 */       end.setTime(date2);
/* 577 */       while (start.compareTo(end) <= 0) {
/* 578 */         String tmp = sformat.format(start.getTime());
/* 579 */         dates.add(tmp);
/* 580 */         start.set(2, start.get(2) + 1);
/*     */       } 
/* 582 */       return dates;
/* 583 */     } catch (ParseException e) {
/* 584 */       e.printStackTrace();
/*     */       
/* 586 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date moveDay(Date date, int day) {
/* 599 */     Calendar cal = Calendar.getInstance();
/* 600 */     cal.setTime(date);
/* 601 */     cal.set(11, 0);
/* 602 */     cal.set(12, 0);
/* 603 */     cal.set(13, 0);
/* 604 */     cal.set(14, 0);
/* 605 */     cal.add(5, day);
/* 606 */     return cal.getTime();
/*     */   }
/*     */ 
/*     */   
/* 610 */   public static String normDate(String input, String iformat) { return normDate(input, iformat, Locale.US, "yyyy-MM-dd HH:mm:ss"); }
/*     */ 
/*     */   
/*     */   public static String normDate(String input, String iformat, Locale local, String oformat) {
/* 614 */     if (StringUtils.isEmpty(input)) {
/* 615 */       return null;
/*     */     }
/* 617 */     Date date = parse(input, iformat, local);
/* 618 */     return (date == null) ? input : format(date, oformat);
/*     */   }
/*     */ 
/*     */   
/* 622 */   public static String normDate(String input, String iformat, String oformat) { return normDate(input, iformat, Locale.US, oformat); }
/*     */ 
/*     */ 
/*     */   
/* 626 */   public static Date parse(String text) { return parse(text, "yyyy-MM-dd HH:mm:ss"); }
/*     */ 
/*     */ 
/*     */   
/* 630 */   public static Date parse(String text, String format) { return parse(text, format, Locale.US); }
/*     */ 
/*     */   
/*     */   public static Date parse(String text, String format, Locale local) {
/*     */     try {
/* 635 */       if (format != null && !"".equals(format) && text != null) {
/* 636 */         SimpleDateFormat formatter = new SimpleDateFormat(format, local);
/* 637 */         return formatter.parse(text);
/*     */       } 
/* 639 */     } catch (Exception e) {}
/*     */     
/* 641 */     return null;
/*     */   }
/*     */   
/*     */   public static Date parseDateyyyyMM(String month) {
/*     */     try {
/* 646 */       return (new SimpleDateFormat("yyyyMM")).parse(month);
/* 647 */     } catch (ParseException e) {
/* 648 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Date parseDateyyyyMMdd(String date) {
/*     */     try {
/* 654 */       return (new SimpleDateFormat("yyyyMMdd")).parse(date);
/* 655 */     } catch (ParseException e) {
/* 656 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date string2Date(String datetext, String format) {
/*     */     try {
/*     */       SimpleDateFormat df;
/* 672 */       if (datetext == null || "".equals(datetext.trim())) {
/* 673 */         return new Date();
/*     */       }
/* 675 */       if (format != null) {
/* 676 */         df = new SimpleDateFormat(format);
/*     */       } else {
/* 678 */         datetext = datetext.replaceAll("/", "-");
/* 679 */         df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */       } 
/* 681 */       return df.parse(datetext);
/* 682 */     } catch (Exception e) {
/* 683 */       e.printStackTrace();
/* 684 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTomorrowLastSecond() {
/* 694 */     Calendar cal = Calendar.getInstance();
/* 695 */     cal.add(5, 1);
/* 696 */     cal.set(11, 23);
/* 697 */     cal.set(13, 59);
/* 698 */     cal.set(12, 59);
/* 699 */     return (int)(cal.getTimeInMillis() / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodayLastSecond() {
/* 708 */     Calendar calendar = Calendar.getInstance();
/* 709 */     calendar.set(11, 23);
/* 710 */     calendar.set(12, 59);
/* 711 */     calendar.set(13, 59);
/* 712 */     long datelong = calendar.getTimeInMillis();
/* 713 */     long datePhp = datelong / 1000L;
/* 714 */     return (int)datePhp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getYesterdayLastSecond() {
/* 725 */     Calendar calendar = Calendar.getInstance();
/* 726 */     calendar.set(11, -1);
/* 727 */     calendar.set(12, 59);
/* 728 */     calendar.set(13, 59);
/* 729 */     long datelong = calendar.getTimeInMillis();
/* 730 */     long datePhp = datelong / 1000L;
/* 731 */     return (int)datePhp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 740 */   public static String getNowTime() { return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 749 */   public static int getHour() { return Calendar.getInstance().get(11); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 758 */   public static int getYear() { return Calendar.getInstance().get(1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 767 */   public static int getMonth() { return Calendar.getInstance().get(2) + 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 776 */   public static int getDay() { return Calendar.getInstance().get(5); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 785 */   public static int getMimute() { return Calendar.getInstance().get(12); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 794 */   public static int getSecond() { return Calendar.getInstance().get(13); }
/*     */ }


/* Location:              C:\Users\Asus\Desktop\to8to\juc\commons-1.3.15.jar!\com\to8to\commo\\util\DateUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.1
 */