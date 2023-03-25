package de.svws_nrw.base.crypto;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Diese Klasse stellt Methoden zum Ver- und Entschlüsseln
 * von Daten mithilfe des AES-Standards zur Verfügung.
 * Der Initialisierungsvektor (IV) wird dabei den verschlüsselten
 * Daten vorangestellt. 
 */
public class AES {

	/** Ein Zufallszahlen-Generator für kryptographisch starke Zufallszahlen (RNG). */
	private static SecureRandom random = new SecureRandom();


	/** Das zu verwendende AES-Verfahren */
	private final AESAlgo algo;
	
	/** Der zu verwendende AES-Schlüssel */
	private final SecretKey key;


	/**
	 * Erstellt ein neues AES-Objekt zum Ver- und entschlüsseln von Daten.
	 *
	 * @param algo   das zu verwendende AES-Verfahren
	 * @param key    der zu verwendende AES-Schlüssel
	 */
	public AES(AESAlgo algo, SecretKey key) {
		this.algo = algo;
		this.key = key;
	}


	/**
	 * Verschlüsselt das übergebene Byte-Array. Der Initialisieruns-
	 * Vektor (IV) wird dabei den Daten vorangestellt. 
	 * 
	 * @param input   das zu verschlüsselnde Byte-Array
	 *
	 * @return das verschlüsselte Byte-Array
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Verschlüsseln auftritt
	 */
	public byte[] encrypt(byte[] input) throws AESException {
		try {
			IvParameterSpec iv = AES.getRandomIV();
		    Cipher cipher = Cipher.getInstance(this.algo.value());
		    cipher.init(Cipher.ENCRYPT_MODE, this.key, iv);
			int len = cipher.getOutputSize(input.length + 16);
			byte[] output = new byte[len];
			byte[] ivData = iv.getIV();
			System.arraycopy(ivData, 0, output, 0, 16);
			cipher.doFinal(input, 0, input.length, output, 16);
		    return output;
		} catch (Exception e) {
			throw new AESException("Fehler beim Verschlüsseln der Daten.", e);
		}
	}


	/**
	 * Entschlüsselt das übergebene Byte Array. Dabei wird davon
	 * ausgegangen, dass dieses Byte-Array den Initialisierungsvektor (IV)
	 * am Anfang des Arrays von den eigentlich verschlüsselten Daten enthält.
	 * 
	 * @param input   die verschlüsselten Daten mit dem IV
	 *
	 * @return die entschlüsselten Daten
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Entschlüsseln auftritt
	 */
	public byte[] decrypt(byte[] input) throws AESException {
		try {
			if (input.length < 16)
				throw new ArrayIndexOutOfBoundsException("Das übegebene Array ist zu klein und kann noch nicht einmal einen Initialisierungsvbektor enthalten.");
			IvParameterSpec iv = new IvParameterSpec(input, 0, 16);
		    Cipher cipher = Cipher.getInstance(this.algo.value());
		    cipher.init(Cipher.DECRYPT_MODE, key, iv);
		    return cipher.doFinal(input, 16, input.length - 16);
		} catch (Exception e) {
			throw new AESException("Fehler beim Entschlüsseln der Daten.", e);
		}
	}

	/**
	 * Verschlüsselt die übergebenen Daten und erstellt einen Base64-kodierten
	 * String aus dem Ergebnis. Der Initialisieruns-
	 * Vektor (IV) wird dabei den Daten vorangestellt.
	 *
	 * @param input   die unverschlüsselten Daten
	 *
	 * @return die verschlüsselten Daten als Base64-String.
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Verschlüsseln auftritt
	 */
	public String encryptBase64(byte[] input) throws AESException {
		return Base64.getEncoder().encodeToString(encrypt(input));
	}


	/**
	 * Entschlüsselt den übergebenen Base64-String. Dabei wird davon
	 * ausgegangen, dass das Base64-kodierte Byte-Array den Initialisierungsvektor (IV)
	 * am Anfang des Arrays von den eigentlich verschlüsselten Daten enthält.
	 *
	 * @param input   die verschlüsselten Base64-kodierten Daten mit dem IV
	 *
	 * @return die entschlüsselten Daten
	 *
	 * @throws AESException   eine Exception, falls ein Fehler beim Entschlüsseln auftritt
	 */
	public byte[] decryptBase64(String input) throws AESException {
		return decrypt(Base64.getDecoder().decode(input));
	}


	/**
	 * Erzeugt einen neuen zufälligen AES-Schlüssel mit der Schlüssellänge 256. 
	 *
	 * @return der AES-Schlüssel
	 *
	 * @throws AESException   falls kein AES-Schlüssel generiert werden kann
	 */
	public static SecretKey getRandomKey256() throws AESException {
		try {
		    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		    keyGenerator.init(256);
		    return keyGenerator.generateKey();
		} catch (Exception e) {
			throw new AESException("Fehler beim Erstellen eines zufälligen AES-Schlüssels.", e);
		}
	}


	/**
	 * Erstellt aus dem angegebenen Kennwort und dem angegebenen Salt einen AES-Schlüssel.
	 * Das bei wird die Password-Based Key Derivation Function 2 (PBDKF2) verwendet.
	 * Dabei kommt Keyed-Hashing for Message Authentication Code (HMAC) in Kombination
	 * mit dem Secure Hash Algorithm (SHA256) zur Verwendung.
	 * 
	 * @param password   das Kennwort
	 * @param salt       der Salt
	 *
	 * @return der AES-Schlüssel
	 *
	 * @throws AESException   falls kein AES-Schlüssel generiert werden kann
	 */
	public static SecretKey getKey256(String password, String salt) throws AESException {
		try {
		    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		    byte[] key = factory.generateSecret(spec).getEncoded();
		    return new SecretKeySpec(key, "AES");
		} catch (Exception e) {
			throw new AESException("Fehler beim Erstellen eines des AES-Schlüssels aus dem Kennwort und dem Salt.", e);
		}
	}


	/**
	 * Erzeugt einen zufälligen Initialisierungsvektor (IV) der Länge 16 Byte
	 * 
	 * @return der IV der Länge 16 Byte
	 */
	private static IvParameterSpec getRandomIV() {
	    byte[] result = new byte[16];
	    random.nextBytes(result);
	    return new IvParameterSpec(result);
	}

}
