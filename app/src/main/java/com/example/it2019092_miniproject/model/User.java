package com.example.it2019092_miniproject.model;

public class User {

    private String NIC;
    private String name;
    private String email;
    private String number;
    private String password;


    public User() {
    }

    public User(String NIC, String name, String email, String number, String password) {
        this.NIC = NIC;
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
    }

    public String getUserNIC() {
        return NIC;
    }

    public void setUserNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
