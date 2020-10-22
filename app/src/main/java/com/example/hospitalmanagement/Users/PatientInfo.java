package com.example.hospitalmanagement.Users;

public class PatientInfo {

    private String PhoneNumber, DateOfBirth, EmailId, Address;
    private String PatientID, Name, SocialSecurityNumber;
    private String BloodPressure,Height,Reason,Visit_Date,Weight;






    public PatientInfo() {

    }


    public PatientInfo(String patientID, String name, String socialSecurityNumber,
                       String Address, String phoneNumber, String dateOfBirth, String emailId, String BloodPressure, String Height,
                       String Reason, String Visit_Date, String Weight) {
        PatientID = patientID;
        Name = name;
        SocialSecurityNumber = socialSecurityNumber;
        PhoneNumber = phoneNumber;
        DateOfBirth = dateOfBirth;
        EmailId = emailId;
        this.Address = Address;
        this.BloodPressure = BloodPressure;
        this.Height = Height;
        this.Reason = Reason;
        this.Visit_Date = Visit_Date;
        this.Weight = Weight;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSocialSecurityNumber() {
        return SocialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        SocialSecurityNumber = socialSecurityNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }
    public String getBloodPressure() {
        return BloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        BloodPressure = bloodPressure;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getVisit_Date() {
        return Visit_Date;
    }

    public void setVisit_Date(String visit_Date) {
        Visit_Date = visit_Date;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }
}
