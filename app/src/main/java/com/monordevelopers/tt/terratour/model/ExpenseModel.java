package com.monordevelopers.tt.terratour.model;

public class ExpenseModel {
    private String aboutExpense;
    private String date;
    private int amount,eventId,expenseID,totalExpense;

    public ExpenseModel(String aboutExpense, int amount,String date, int eventId, int expenseID) {
        this.aboutExpense = aboutExpense;
        this.amount = amount;
        this.eventId = eventId;
        this.expenseID = expenseID;
        this.date=date;
        totalExpense+=amount;
    }
    public ExpenseModel(String aboutExpense, int amount,String date, int eventId) {
        this.aboutExpense = aboutExpense;
        this.amount = amount;
        this.eventId = eventId;
        this.date=date;
    }

    public String getAboutExpense() {
        return aboutExpense;
    }

    public int getAmount() {
        return amount;
    }

    public int getEventId() {
        return eventId;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public String getDate() {
        return date;
    }

    public int getTotalExpense() {
        return totalExpense;
    }
}
