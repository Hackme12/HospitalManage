package com.example.hospitalmanagement.Users;

public class PatientInfo {

    private String PatientID, Name, SocialSecurityNumber;


    private String PhoneNumber, DateOfBirth, EmailId, Address;

    public PatientInfo() {

    }


    public PatientInfo(String patientID, String name, String socialSecurityNumber, String Address, String phoneNumber, String dateOfBirth, String emailId) {
        PatientID = patientID;
        Name = name;
        SocialSecurityNumber = socialSecurityNumber;
        PhoneNumber = phoneNumber;
        DateOfBirth = dateOfBirth;
        EmailId = emailId;
        this.Address = Address;
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
}
