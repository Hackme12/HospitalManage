package com.example.hospitalmanagement.Users;

public class DailyReport {
    private String DrName,date;
    private Long TotalPatientVisitToday;
    public DailyReport(){}
    public DailyReport(String drName, Long totalPatientVisitToday, String date) {
        DrName = drName;
        TotalPatientVisitToday = totalPatientVisitToday;
        this.date = date;
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


}
