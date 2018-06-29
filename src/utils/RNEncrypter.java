package utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class RNEncrypter {
	
	private static final String ENCSK = "g0s1n2";
	
	 // 8-byte Salt
    private static byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };

    // Iteration count
    private static int iterationCount = 19;

	public static String encrypt(String decValue) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException{
        KeySpec ks = new PBEKeySpec(ENCSK.toCharArray());
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(ks);
        
        Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt, iterationCount));

        byte[] encArray = ecipher.doFinal(decValue.getBytes("UTF-8"));
        
        return Base64.encodeBase64String(encArray);
	}
	
	public static String decrypt(String encValue) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
        KeySpec ks = new PBEKeySpec(ENCSK.toCharArray());
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(ks);
        Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(salt, iterationCount));

        byte[] decArray = Base64.decodeBase64(encValue);
        
        return new String(dcipher.doFinal(decArray), "UTF-8");
	}
	
	public static void main(String[] args) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException{
		System.out.println("----------------------------");		
		System.out.println("---  Encriptar passwords ---");
		System.out.println("----------------------------");		
		System.out.println();
		if (args.length > 0){
			System.out.println("Password encriptada:");
			System.out.println();
			System.out.println(encrypt(args[0]));
		}else{
			System.out.println("Faltan parámetros.");
			System.out.println();
			System.out.println("Uso: java -cp cferos.jar org.gs1uy.cferos.security.RNEncrypter <password>");
		}
	}
}
