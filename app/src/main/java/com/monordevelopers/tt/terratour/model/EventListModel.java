package com.monordevelopers.tt.terratour.model;

/**
 * Created by izajul on 2/5/2017.
 */

public class EventListModel {
    private String destination,budget,fromDate,toDate;
    int id,uid,totalExpense;

    public EventListModel(String destination, String budget, String fromDate, String toDate,int uid, int id,int totalExpense) {
        this.destination = destination;
        this.budget = budget;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.id = id;
        this.uid = uid;
        this.totalExpense = totalExpense;
    }
    public EventListModel(String destination, String budget, String fromDate, String toDate,int uid,int totalExpense) {
        this.destination = destination;
        this.budget = budget;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.uid = uid;
        this.totalExpense = totalExpense;
    }
    public EventListModel(String destination, String budget, String fromDate, String toDate,int uid) {
        this.destination = destination;
        this.budget = budget;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.uid = uid;
    }

    public String getDestination() {
        return destination;
    }

    public String getBudget() {
        return budget;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public int getId() {
        return id;
    }

    public int getUid() {
        return uid;
    }

    public int getTotalExpense() {
        return totalExpense;
    }
}
