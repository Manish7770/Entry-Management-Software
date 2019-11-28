package com.example.reception.Model;

public class NewHostModel {

    private String Name,
            Email,
            Address,
            Phone;

    public NewHostModel() {
    }

    public NewHostModel(String name, String email, String address, String phone) {
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
}
