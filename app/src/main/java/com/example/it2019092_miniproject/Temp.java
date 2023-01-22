package com.example.it2019092_miniproject;

public class Temp {
    private static String NIC;
    private static String packageID;


    public static String getNIC() {
        return NIC;
    }

    public static void setNIC(String NIC) {
        Temp.NIC = NIC;
    }

    public static String getPackageID() {
        return packageID;
    }

    public static void setPackageID(String packageID) {
        Temp.packageID = packageID;
    }


}