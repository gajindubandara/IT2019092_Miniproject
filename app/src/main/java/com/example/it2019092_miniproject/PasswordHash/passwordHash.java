package com.example.it2019092_miniproject.PasswordHash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class passwordHash {
    public static String getMd5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashpwd = no.toString(16);
            while (hashpwd.length() < 32) {
                hashpwd = "0" + hashpwd;
            }
            return hashpwd;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}