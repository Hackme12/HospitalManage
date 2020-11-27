package com.example.hospitalmanagement.Users;

public class DailyReport {
    private String DrName,date;
    private Long TotalPatientVisitToday;
    private double Total_Amount;
    public DailyReport(){}
    public DailyReport(String drName, Long totalPatientVisitToday, String date, double Total_Amount) {
        DrName = drName;
        TotalPatientVisitToday = totalPatientVisitToday;
        this.date = date;
        this.Total_Amount = Total_Amount;
    }


    public String getDrName() {
        return DrName;
    }

    public void setDrName(String drName) {
        DrName = drName;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTotalPatientVisitToday() {
        return TotalPatientVisitToday;
    }

    public void setTotalPatientsVisitToday(Long totalPatientVisitToday) {
        TotalPatientVisitToday = totalPatientVisitToday;
    }
    public double getTotal_Amount(){
        return Total_Amount;
    }
    public void setTotal_Amount(double Total_Amount){
        this.Total_Amount = Total_Amount;
    }



}
