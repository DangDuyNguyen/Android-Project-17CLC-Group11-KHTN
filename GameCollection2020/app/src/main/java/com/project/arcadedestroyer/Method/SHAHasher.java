package com.project.arcadedestroyer.Method;

import java.security.MessageDigest;

public class SHAHasher {
    public static String hashing(String str)
    {
        MessageDigest digest = null;
        String hashed = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(str.getBytes());

            hashed = bytesToHexString(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashed;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
