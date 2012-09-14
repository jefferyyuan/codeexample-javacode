package org.codeexample.jefferyyuan.javacode.security;

import java.io.FileInputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class ComputeMAC
{
    public static void main(
            String[] unused) throws Exception
    {
        String datafile = "ComputeDigest.java";

        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(56); // 56 is the keysize. Fixed for DES
        SecretKey key = kg.generateKey();

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(key);

        FileInputStream fis = new FileInputStream(datafile);
        byte[] dataBytes = new byte[1024];
        int nread = fis.read(dataBytes);
        while (nread > 0)
        {
            mac.update(dataBytes, 0, nread);
            nread = fis.read(dataBytes);
        }
        ;
        byte[] macbytes = mac.doFinal();
        // System.out.println("MAC(in hex):: " + Util.byteArray2Hex(macbytes));
    }
}