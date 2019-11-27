package com.example.visitormanagement.Model;

public class HostModel {

    private String Name,
            Email,
            Address,
            Phone;
    private int available;

    public HostModel() {
        Name="";
        Email="";
        Address="";
        Phone="";
    }

    public HostModel(String name, String email, String address, String phone, int available) {
        Name = name;
        Email = email;
        Address = address;
        Phone = phone;
        this.available = available;
    }

    public HostModel(String name, String email, String address, String phone) {
        Name = name;
        Email = email;
        Address = address;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
