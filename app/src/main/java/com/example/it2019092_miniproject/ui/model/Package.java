package com.example.it2019092_miniproject.ui.model;

public class Package {
    private String packageID;
    private String place;
    private String date;
    private String price;
    private String non;
    private String nod;
    private String des;
    private String coverImg;

    public Package() {
    }

    public Package(String packageID, String place, String date, String price, String non, String nod, String des, String coverImg) {
        this.packageID = packageID;
        this.place = place;
        this.date = date;
        this.price = price;
        this.non = non;
        this.nod = nod;
        this.des = des;
        this.coverImg = coverImg;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNon() {
        return non;
    }

    public void setNon(String non) {
        this.non = non;
    }

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}
