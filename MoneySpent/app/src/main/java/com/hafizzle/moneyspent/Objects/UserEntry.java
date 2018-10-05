package com.hafizzle.moneyspent.Objects;

public class UserEntry {
    private String dateStamp;
    private double moneyAmount;


    public UserEntry(String dateStamp, double moneyAmount) {
        this.dateStamp = dateStamp;
        this.moneyAmount = moneyAmount;
    }


    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }
}
