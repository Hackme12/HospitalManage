package com.example.hospitalmanagement.Users;

import com.example.hospitalmanagement.Appointment;

public class AppointmentInformation {

    public String date, time, doctorName, patientName, patientId;

    public AppointmentInformation(){

    }

    public AppointmentInformation(String date, String time, String doctorName, String patientName, String patientId) {
        this.date = date;
        this.time = time;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.patientId = patientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
