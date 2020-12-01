package com.example.hospitalmanagement.Users;

public class checkList {

    private String PatientId, Name,Amount, Date,Status;



    public checkList() {
    }
    public checkList(String patientId, String name, String amount, String date, String status) {
        PatientId = patientId;
        Name = name;
        Amount = amount;
        Date = date;
        Status = status;
    }
    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPatientId() {
        return PatientId;
    }

    public String getName() {
        return Name;
    }

    public String getAmount() {
        return Amount;
    }

    public String getDate() {
        return Date;
    }

    public String getStatus() {
        return Status;
    }
}
