package com.wywallet.multipay.merchant;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * Created by 4t-johoi on 09/09/14.
 */
public class Sha1Hash {


    public Sha1Hash() {
    }

    public String getHashedValue(String inputData) {
        try {
            byte byteHash[];
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(inputData.getBytes("utf-8"));
            byteHash = sha1.digest();
            sha1.reset();

            Base64.Encoder encoder = Base64.getEncoder();
            String hash = encoder.encodeToString(byteHash);

            return hash.substring(0, (hash.length() / 2));
        } catch (Exception e) {
            System.err.println("getHashedValue failed: " + e.getMessage());
            return null;
        }
    }
}
