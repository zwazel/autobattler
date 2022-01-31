package dev.zwazel.autobattler.classes.Utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static byte[] getSHA(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        return md.digest(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String getHexString(byte[] hash) {
        BigInteger num = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(num.toString(16));

        while (hexString.length() < 32)
            hexString.insert(0, '0');

        return hexString.toString();
    }

    public static String getHexStringInstant(String string) {
        return getHexString(getSHA(string));
    }
}
