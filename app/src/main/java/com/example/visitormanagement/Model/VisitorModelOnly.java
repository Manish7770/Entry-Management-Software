package com.example.visitormanagement.Model;

public class VisitorModelOnly {

    private String Name,
            Email,
            Phone,
            Token;

    public VisitorModelOnly() {
    }

    public VisitorModelOnly(String name, String email, String phone, String token) {
        Name = name;
        Email = email;
        Phone = phone;
        Token = token;
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

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
