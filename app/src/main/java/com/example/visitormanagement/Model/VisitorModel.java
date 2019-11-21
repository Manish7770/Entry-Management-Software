package com.example.visitormanagement.Model;

public class VisitorModel {

    private String Name,
            Email,
            Phone,
            Token;

    private HostModel host;

    public VisitorModel() {
        Name = "";
        Email = "";
        Phone = "";
        Token = "";
        this.host = new HostModel();
    }

    public VisitorModel(String name, String email, String phone, String token, HostModel host) {
        Name = name;
        Email = email;
        Phone = phone;
        Token = token;
        this.host = host;
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

    public HostModel getHost() {
        return host;
    }

    public void setHost(HostModel host) {
        this.host = host;
    }
}
