package com.cmpt276.kenneyw.carbonfootprinttracker.model;

/**
 * Creates a DateSingleton. There can be only one.
 * The singleton will keep it's values somewhat persistently, which is useful for calls to it's values later.
 */

import java.util.Date;
public class DateSingleton {
    private static DateSingleton dateinstance = null;
    private DateSingleton(){
        this.dateString="";
    }
    public static DateSingleton getInstance(){
        if(dateinstance == null){
            dateinstance = new DateSingleton();
        }
        return dateinstance;
    }
    private int year;
    private int month;
    private int day;
    private String dateString;
    private Date date;
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setDateString(String date) {
        this.dateString = date;
    }
    public String getDateString() {
        return dateString;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}