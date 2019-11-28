package com.example.visitormanagement.Model;

public class VisitedOutModel {
    private String Name,
            Email,
            Phone,
            CheckOut;

    public VisitedOutModel() {
    }

    public VisitedOutModel(String name, String email, String phone, String checkOut) {
        Name = name;
        Email = email;
        Phone = phone;
        CheckOut = checkOut;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String checkOut) {
        CheckOut = checkOut;
    }
}
