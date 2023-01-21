package com.example.it2019092_miniproject.model;

public class Booking {

    private String ID;
    private String packageId;
    private String bDate;
    private String userId;
    private String nop;
    private String total;
    private String status;

    public Booking() {
    }

    public Booking(String ID, String packageId, String bDate, String userId, String nop, String total, String status) {
        this.ID = ID;
        this.packageId = packageId;
        this.bDate = bDate;
        this.userId = userId;
        this.nop = nop;
        this.total = total;
        this.status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNop() {
        return nop;
    }

    public void setNop(String nop) {
        this.nop = nop;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
