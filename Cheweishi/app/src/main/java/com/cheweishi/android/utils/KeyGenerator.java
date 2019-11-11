/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cheweishi.android.utils;

import android.util.Base64;

import com.cheweishi.android.config.Constant;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 *
 * @author tallauze
 */
public final class KeyGenerator {

	public static final String ALGORITHM = "RSA";

	public static final String CIPHER_DECRYPT_ALGORYTHM = "RSA/ECB/PKCS1Padding";
	public static final String CIPHER_ENCRYPT_ALGORYTHM = "RSA/ECB/PKCS1Padding";

	public static KeyPair generateKeys() {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance(ALGORITHM);
			kpg.initialize(1024);
		} catch (NoSuchAlgorithmException ex) {
            LogHelper.e(ex.getMessage());
		}
		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}

    private KeyGenerator() {

    }

    /**
     * 加密
     * @param text
     * @return
     * @throws Exception
     */
    public static String encrypt(String text)  {
		PublicKey key = null;
		try {
			key = getPublicKey(Constant.SERVER_PUBLIC_KEY_STORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt(text, key);
    }

    /**
     * 获取公钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key, Base64.DEFAULT);
        KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 加密
     *
     * @param text
     * @param key
     * @return
     */
	private static String encrypt(String text, PublicKey key) {
		Cipher cipher = null;
		byte[] cipherText = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ENCRYPT_ALGORYTHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String temp = null;
		try {
            temp = Base64.encodeToString(cipherText, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

//    /**
//     * 解密
//     * @param text
//     * @param key
//     * @return
//     */
//	public static String decrypt(String text, PrivateKey key) {
//		byte[] cryptedText = Base64.decodeBase64(text);
//		byte[] decryptedText = null;
//		Cipher cipher = null;
//		String temp = null;
//		try {
//			cipher = Cipher.getInstance(CIPHER_DECRYPT_ALGORYTHM);
//			cipher.init(Cipher.DECRYPT_MODE, key);
//			decryptedText = cipher.doFinal(cryptedText);
//			temp = new String(decryptedText);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		return temp;
//	}
}
