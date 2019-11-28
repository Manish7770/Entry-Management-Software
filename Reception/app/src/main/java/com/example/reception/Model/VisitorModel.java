package com.example.reception.Model;

public class VisitorModel {

    private String VisitorName,
            VisEmail,
            VisPhone,
            HostName,
            CheckInTime,
            CheckOutTime;

    private Long timestmp;

    public VisitorModel() {
    }

    public VisitorModel(String visitorName, String visEmail, String visPhone, String hostName, String checkInTime, String checkOutTime, Long timestmp) {
        VisitorName = visitorName;
        VisEmail = visEmail;
        VisPhone = visPhone;
        HostName = hostName;
        CheckInTime = checkInTime;
        CheckOutTime = checkOutTime;
        this.timestmp = timestmp;
    }

    public String getVisitorName() {
        return VisitorName;
    }

    public void setVisitorName(String visitorName) {
        VisitorName = visitorName;
    }

    public String getVisEmail() {
        return VisEmail;
    }

    public void setVisEmail(String visEmail) {
        VisEmail = visEmail;
    }

    public String getVisPhone() {
        return VisPhone;
    }

    public void setVisPhone(String visPhone) {
        VisPhone = visPhone;
    }

    public String getHostName() {
        return HostName;
    }

    public void setHostName(String hostName) {
        HostName = hostName;
    }

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        CheckOutTime = checkOutTime;
    }

    public Long getTimestmp() {
        return timestmp;
    }

    public void setTimestmp(Long timestmp) {
        this.timestmp = timestmp;
    }
}
