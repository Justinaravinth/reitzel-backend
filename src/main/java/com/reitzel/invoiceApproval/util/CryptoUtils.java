package com.reitzel.invoiceApproval.util;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.reitzel.invoiceApproval.common.CommonConstant;

import net.iharder.Base64;

@Component
public class CryptoUtils {

	public static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtils.class);
	private static final String ENCRYPTION_KEY = "ABCDEFGHIJKLMNOP";
	private static final String CHARACTER_ENCODING = "UTF-8";
	private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";
	private static final String AES_ENCRYPTION_ALGORITHEM = "AES";

	@Autowired
	PasswordEncoder encoder;

	/**
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String getDecrypt(String password) throws Exception {
		String methodName = "getDecrypt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String decryptedPasswd = null;
		String errorMsg;
		byte[] bytes;
		try {
			String afterDecrypt = decrypt(password);
			bytes = Hex.decodeHex(afterDecrypt.toCharArray());
		} catch (DecoderException e) {
			errorMsg = e.getMessage();
			LOGGER.error(errorMsg);
			throw new ApplicationContextException(errorMsg);
		}
		try {
			decryptedPasswd = new String(bytes, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationContextException(e.getMessage());
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return decryptedPasswd;
	}

	/**
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(final String encrypted) throws Exception {
		String methodName = "decrypt()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		try {
			SecretKey key = new SecretKeySpec(Base64.decode("u/Gu5posvwDsXUnV5Zaq4g=="), "AES");
			AlgorithmParameterSpec iv = new IvParameterSpec(Base64.decode("5D9r9ZVzEYYgha93/aUK2w=="));
			byte[] decodeBase64 = Base64.decode(encrypted);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			return new String(cipher.doFinal(decodeBase64), StandardCharsets.UTF_8.toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationContextException(e.getMessage());
		}
	}

	public static String encrypt(String plainText) {
		String encryptedText = StringUtils.EMPTY;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			byte[] key = Base64.decode("u/Gu5posvwDsXUnV5Zaq4g==");
			byte[] ivparam = Base64.decode("5D9r9ZVzEYYgha93/aUK2w==");
			SecretKeySpec secretKey = new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHEM);
			IvParameterSpec ivparameterspec = new IvParameterSpec(ivparam);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
			plainText = Hex.encodeHexString(plainText.getBytes(StandardCharsets.UTF_8));
			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
			encryptedText = encoder.encodeToString(cipherText);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return encryptedText;
	}

	public static String encryptWithJavaBase64(String plainText) {
		String encryptedText = StringUtils.EMPTY;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			byte[] key = ENCRYPTION_KEY.getBytes(CHARACTER_ENCODING);
			SecretKeySpec secretKey = new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHEM);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
			encryptedText = encoder.encodeToString(cipherText);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return encryptedText;
	}

	public static String decryptWithJavaBase64(String encryptedText) {
		String decryptedText = StringUtils.EMPTY;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			byte[] key = ENCRYPTION_KEY.getBytes(CHARACTER_ENCODING);
			SecretKeySpec secretKey = new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHEM);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
			java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
			byte[] cipherText = decoder.decode(encryptedText.getBytes(StandardCharsets.UTF_8));
			decryptedText = new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return decryptedText;
	}

}
