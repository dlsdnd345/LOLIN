package com.iris.libs;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;

public class TrippleDes {
	private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    byte[] ivValues;
    private String myEncryptionScheme;
    SecretKey key;

/**
 *  과거에는 Base64 jar을 넣고 실행 했지만 ,
 *  현재는 이클립스에서 Base64 를 지원해준다
 */
    public TrippleDes() throws Exception {
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = new byte[] {0x4d, 0x61, 0x6b, 0x65, 0x20, 0x42, 0x79, 0x20, 0x4b, 0x41, 0x48, 0x41
                , 0x20, 0x48, 0x6f, 0x6c, 0x64, 0x69, 0x6e, 0x67, 0x73, 0x20, 0x53, 0x6f };
        ivValues = new byte[] {0x00, (byte) 0x84, 0x54, 0x04, (byte) 0xc4, 0x00, 0x54, 0x04};
        ks = new DESedeKeySpec(arrayBytes);        
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");        
        key = skf.generateSecret(ks);
    }

    /**
     * encrypt 메서드는 함호화 제공
     * @param unencryptedString : 암호화할 문자열 
     * @return encryptedString : 암호화된 문자열
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {        	
            IvParameterSpec iv = new IvParameterSpec(ivValues);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            
            encryptedString = new String(Base64.encodeToString(encryptedText, Base64.NO_WRAP));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    /**
     * encrypt 메서드는 복호화 제공
     * @param encryptedString :  복호화할 문자열 
     * @return decryptedText : 복호화된 문자열
     */
    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            IvParameterSpec iv = new IvParameterSpec(ivValues);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            
            byte[] encryptedText = Base64.decode(encryptedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText, UNICODE_FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}
