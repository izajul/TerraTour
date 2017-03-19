
package com.monordevelopers.tt.terratour.weatherPojo.hourlyforecastpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCTTIME {

    @SerializedName("hour")
    @Expose
    private String hour;
    @SerializedName("hour_padded")
    @Expose
    private String hourPadded;
    @SerializedName("min")
    @Expose
    private String min;
    @SerializedName("min_unpadded")
    @Expose
    private String minUnpadded;
    @SerializedName("sec")
    @Expose
    private String sec;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("mon")
    @Expose
    private String mon;
    @SerializedName("mon_padded")
    @Expose
    private String monPadded;
    @SerializedName("mon_abbrev")
    @Expose
    private String monAbbrev;
    @SerializedName("mday")
    @Expose
    private String mday;
    @SerializedName("mday_padded")
    @Expose
    private String mdayPadded;
    @SerializedName("yday")
    @Expose
    private String yday;
    @SerializedName("isdst")
    @Expose
    private String isdst;
    @SerializedName("epoch")
    @Expose
    private String epoch;
    @SerializedName("pretty")
    @Expose
    private String pretty;
    @SerializedName("civil")
    @Expose
    private String civil;
    @SerializedName("month_name")
    @Expose
    private String monthName;
    @SerializedName("month_name_abbrev")
    @Expose
    private String monthNameAbbrev;
    @SerializedName("weekday_name")
    @Expose
    private String weekdayName;
    @SerializedName("weekday_name_night")
    @Expose
    private String weekdayNameNight;
    @SerializedName("weekday_name_abbrev")
    @Expose
    private String weekdayNameAbbrev;
    @SerializedName("weekday_name_unlang")
    @Expose
    private String weekdayNameUnlang;
    @SerializedName("weekday_name_night_unlang")
    @Expose
    private String weekdayNameNightUnlang;
    @SerializedName("ampm")
    @Expose
    private String ampm;
    @SerializedName("tz")
    @Expose
    private String tz;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("UTCDATE")
    @Expose
    private String uTCDATE;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHourPadded() {
        return hourPadded;
    }

    public void setHourPadded(String hourPadded) {
        this.hourPadded = hourPadded;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMinUnpadded() {
        return minUnpadded;
    }

    public void setMinUnpadded(String minUnpadded) {
        this.minUnpadded = minUnpadded;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getMonPadded() {
        return monPadded;
    }

    public void setMonPadded(String monPadded) {
        this.monPadded = monPadded;
    }

    public String getMonAbbrev() {
        return monAbbrev;
    }

    public void setMonAbbrev(String monAbbrev) {
        this.monAbbrev = monAbbrev;
    }

    public String getMday() {
        return mday;
    }

    public void setMday(String mday) {
        this.mday = mday;
    }

    public String getMdayPadded() {
        return mdayPadded;
    }

    public void setMdayPadded(String mdayPadded) {
        this.mdayPadded = mdayPadded;
    }

    public String getYday() {
        return yday;
    }

    public void setYday(String yday) {
        this.yday = yday;
    }

    public String getIsdst() {
        return isdst;
    }

    public void setIsdst(String isdst) {
        this.isdst = isdst;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public String getPretty() {
        return pretty;
    }

    public void setPretty(String pretty) {
        this.pretty = pretty;
    }

    public String getCivil() {
        return civil;
    }

    public void setCivil(String civil) {
        this.civil = civil;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthNameAbbrev() {
        return monthNameAbbrev;
    }

    public void setMonthNameAbbrev(String monthNameAbbrev) {
        this.monthNameAbbrev = monthNameAbbrev;
    }

    public String getWeekdayName() {
        return weekdayName;
    }

    public void setWeekdayName(String weekdayName) {
        this.weekdayName = weekdayName;
    }

    public String getWeekdayNameNight() {
        return weekdayNameNight;
    }

    public void setWeekdayNameNight(String weekdayNameNight) {
        this.weekdayNameNight = weekdayNameNight;
    }

    public String getWeekdayNameAbbrev() {
        return weekdayNameAbbrev;
    }

    public void setWeekdayNameAbbrev(String weekdayNameAbbrev) {
        this.weekdayNameAbbrev = weekdayNameAbbrev;
    }

    public String getWeekdayNameUnlang() {
        return weekdayNameUnlang;
    }

    public void setWeekdayNameUnlang(String weekdayNameUnlang) {
        this.weekdayNameUnlang = weekdayNameUnlang;
    }

    public String getWeekdayNameNightUnlang() {
        return weekdayNameNightUnlang;
    }

    public void setWeekdayNameNightUnlang(String weekdayNameNightUnlang) {
        this.weekdayNameNightUnlang = weekdayNameNightUnlang;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUTCDATE() {
        return uTCDATE;
    }

    public void setUTCDATE(String uTCDATE) {
        this.uTCDATE = uTCDATE;
    }

}
