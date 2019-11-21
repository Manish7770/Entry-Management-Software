package com.example.visitormanagement.Model;

public class VisitedOutModel {
    private String Name,
            Email,
            Phone;

    public VisitedOutModel() {
    }

    public VisitedOutModel(String name, String email, String phone) {
        Name = name;
        Email = email;
        Phone = phone;
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
}
