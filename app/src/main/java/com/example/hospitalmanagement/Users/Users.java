package com.example.hospitalmanagement.Users;

public class Users {
    private String Name, DateofBirth, Email, UserId, Password, ConfirmPassword;

    public Users() {

    }


    public Users(String name, String dateOfBirth, String email, String userId, String password, String confirm_Password) {
        this.Name = name;
        this.DateofBirth = dateOfBirth;
        this.Email = email;
        this.UserId = userId;
        this.Password = password;
        this.ConfirmPassword = confirm_Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDateOfBirth() {
        return DateofBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateofBirth = dateOfBirth;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirm_Password() {
        return ConfirmPassword;
    }

    public void setConfirm_Password(String confirm_Password) {
        ConfirmPassword = confirm_Password;
    }
}
