package de.svws_nrw.base.crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Diese Klasse stellt Methoden zum Erzeugen von RSA-Schlüsselpaaren
 * sowie zum Ver- und Entschlüsseln von Daten mithilfe des RSA-Verfahrens
 * zur Verfügung. Dabei wird das RSA-Verfahren mit dem AES-Verfahren kombiniert.
 * Beim Verschlüsseln wird ein zufälliger AES-256 generiert. Dieser wird dann mit
 * dem RSA-Verfahren verschlüsselt und den Daten vorangestellt. Die eigentlich
 * zu transportierenden Daten werden dann angehängt und werden mit diesem
 * AES-Schlüssel und dem Vefahren {@link AESAlgo#CBC_PKCS5PADDING} verschlüsselt.
 */
public class RSA {

	/** Die Default-Bit-Länge für neu erzeugte RSA-Schlüsselpaare */
	public static final int DEFAULT_KEY_LENGTH = 8192;

	/** Der zu verwendende private RSA-Schlüssel oder null, wenn dieser nicht verfügbar ist */
	private final PrivateKey privateKey;

	/** Der zu verwendende öffentliche RSA-Schlüssel oder null, wenn dieser nicht verfügbar ist */
	private final PublicKey publicKey;


	/**
	 * Erstellt ein neues RSA-Objekt zum Ver- und Entschlüsseln von Daten.
	 *
	 * @param privateKey   der zu verwendende private RSA-Schlüssel oder null
	 * @param publicKey    der zu verwendende öffentliche RSA-Schlüssel oder null
	 */
	public RSA(final PrivateKey privateKey, final PublicKey publicKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}


	/**
	 * Verschlüsselt das übergebene Byte-Array mithilfe einer Kombination von AES und RSA.
	 *
	 * @param input   das zu verschlüsselnde Byte-Array
	 *
	 * @return das verschlüsselte Byte-Array
	 *
	 * @throws RSAException   eine Exception, falls ein Fehler beim Verschlüsseln auftritt
	 */
	public byte[] encrypt(final byte[] input) throws RSAException {
		if (this.publicKey == null)
			throw new RSAException("Fehler beim Verschlüsseln der Daten: Es steht kein öffentlicher Schlüssel zur Verfügung.");
		try {
			// Erstelle einen zufälligen AES-Schlüssel ...
			final SecretKey aeskey = AES.getRandomKey256();
			// ... dann verschlüssele den AES-Schlüssel mit dem RSA-Verfahren.
			final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
			final byte[] aeskeyEncoded = cipher.doFinal(aeskey.getEncoded());
			// ... anschließend verschlüssele die Daten mit dem AES-Verfahren
			final AES aes = new AES(AESAlgo.CBC_PKCS5PADDING, aeskey);
			final byte[] tmp = aes.encrypt(input);
			// ... und kombiniere dann die beiden byte-Arrays zu einem großen ...
			final byte[] result = new byte[aeskeyEncoded.length + tmp.length];
			System.arraycopy(aeskeyEncoded, 0, result, 0, aeskeyEncoded.length);
			System.arraycopy(tmp, 0, result, aeskeyEncoded.length, tmp.length);
			return result;
		} catch (final Exception e) {
			throw new RSAException("Fehler beim Verschlüsseln der Daten.", e);
		}
	}


	/**
	 * Entschlüsselt das übergebene Byte Array mithilfe einer Kombination von AES und RSA.
	 *
	 * @param input   die verschlüsselten Daten
	 *
	 * @return die entschlüsselten Daten
	 *
	 * @throws RSAException   eine Exception, falls ein Fehler beim Entschlüsseln auftritt
	 */
	public byte[] decrypt(final byte[] input) throws RSAException {
		if (this.privateKey == null)
			throw new RSAException("Fehler beim Entschlüsseln der Daten: Es steht kein privater Schlüssel zur Verfügung.");
		try {
			// Stelle den AES-Schlüssel mithilfe des Blockes am Anfang der Daen wieder her
			final int aeskeyEncodedLength = DEFAULT_KEY_LENGTH / 8;
			if (input.length < aeskeyEncodedLength)
				throw new RSAException("Die Daten enthalten keinen mit RSA verschlüsselten AES-Schlüssel.");
			final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
			cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
			final byte[] aeskeyDecoded = cipher.doFinal(input, 0, aeskeyEncodedLength);
			// ... und dekodiere den Rest
			final int dataLength = input.length - aeskeyEncodedLength;
			final byte[] tmp = new byte[dataLength];
			System.arraycopy(input, aeskeyEncodedLength, tmp, 0, dataLength);
			final AES aes = new AES(AESAlgo.CBC_PKCS5PADDING, AES.getKeyFromByteArray(aeskeyDecoded));
			return aes.decrypt(tmp);
		} catch (final Exception e) {
			throw new RSAException("Fehler beim Entschlüsseln der Daten.", e);
		}
	}


	/**
	 * Verschlüsselt die übergebenen Daten mithilfe einer Kombination von AES und RSA und erstellt
	 * einen Base64-kodierten String aus dem Ergebnis.
	 *
	 * @param input   die unverschlüsselten Daten
	 *
	 * @return die verschlüsselten Daten als Base64-String.
	 *
	 * @throws RSAException   eine Exception, falls ein Fehler beim Verschlüsseln auftritt
	 */
	public String encryptBase64(final byte[] input) throws RSAException {
		return Base64.getEncoder().encodeToString(encrypt(input));
	}


	/**
	 * Entschlüsselt den übergebenen Base64-String mithilfe einer Kombination von AES und RSA.
	 *
	 * @param input   die verschlüsselten Base64-kodierten Daten
	 *
	 * @return die entschlüsselten Daten
	 *
	 * @throws RSAException   eine Exception, falls ein Fehler beim Entschlüsseln auftritt
	 */
	public byte[] decryptBase64(final String input) throws RSAException {
		return decrypt(Base64.getDecoder().decode(input));
	}



	/**
	 * Erzeugt ein neues RSA-Schlüssel-Paar mit der Default-Schlüssellänge
	 * (siehe {@link RSA#DEFAULT_KEY_LENGTH}.
	 *
	 * @return das RSA-Schlüsselpaar
	 *
	 * @throws RSAException   falls kein RSA-Schlüssel generiert werden kann
	 */
	public static KeyPair createKey() throws RSAException {
		try {
			final KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(DEFAULT_KEY_LENGTH);
			return keyGenerator.generateKeyPair();
		} catch (final Exception e) {
			throw new RSAException("Fehler beim Erstellen eines zufälligen RSA-Schlüssels.", e);
		}
	}


	/**
	 * Erzeugt ein neues RSA-Schlüssel-Paar mit der angegeben Schlüssellänge.
	 *
	 * @param length   die Schlüssellänge
	 *
	 * @return das RSA-Schlüsselpaar
	 *
	 * @throws RSAException   falls kein RSA-Schlüssel generiert werden kann
	 */
	public static KeyPair createKeyWithLength(final int length) throws RSAException {
		try {
			final KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
			keyGenerator.initialize(length);
			return keyGenerator.generateKeyPair();
		} catch (final Exception e) {
			throw new RSAException("Fehler beim Erstellen eines zufälligen RSA-Schlüssels.", e);
		}
	}


	/**
	 * Gibt den Schlüssel in Base-64-Kodierung zurück.
	 *
	 * @param key   der Schlüssel
	 *
	 * @return der Schlüssel in Base-64-Kodierung
	 */
	public static String encodeKey(final Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}


	/**
	 * Gibt den öffentlichen Schlüssel in Base-64-Kodierung zurück.
	 *
	 * @return der öffentliche Schlüssel in Base-64-Kodierung
	 *
	 * @throws RSAException falls der Schlüssel nicht vorhanden ist (null)
	 */
	public String encodePublicKey() throws RSAException {
		if (this.publicKey == null)
			throw new RSAException("Der öffentliche Schlüssel ist nicht vorhanden.");
		return encodeKey(this.publicKey);
	}


	/**
	 * Gibt den privaten Schlüssel in Base-64-Kodierung zurück.
	 *
	 * @return der private Schlüssel in Base-64-Kodierung
	 *
	 * @throws RSAException falls der Schlüssel nicht vorhanden ist (null)
	 */
	public String encodePrivateKey() throws RSAException {
		if (this.privateKey == null)
			throw new RSAException("Der private Schlüssel ist nicht vorhanden.");
		return encodeKey(this.privateKey);
	}


	/**
	 * Wandelt den Base-64-Kodierung Schlüssel in den zugehörigen
	 * PublicKey um.
	 *
	 * @param key   der Base-64-kodierte Schlüssel
	 *
	 * @return der öffentliche Schlüssel
	 *
	 * @throws RSAException falls der Schlüssel nicht erfolgreich dekodiert werden kann
	 */
	public static PublicKey decodePublicKey(final String key) throws RSAException {
		try {
			final byte[] keyBytes = Base64.getDecoder().decode(key);
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
		} catch (final InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RSAException("Fehler beim Dekodieren des öffentlichen RSA-Schlüssels.", e);
		}
	}

	/**
	 * Wandelt den Base-64-Kodierung Schlüssel in den zugehörigen
	 * PrivateKey um.
	 *
	 * @param key   der Base-64-kodierte Schlüssel
	 *
	 * @return der private Schlüssel
	 *
	 * @throws RSAException falls der Schlüssel nicht erfolgreich dekodiert werden kann
	 */
	public static PrivateKey decodePrivateKey(final String key) throws RSAException {
		try {
			final byte[] keyBytes = Base64.getDecoder().decode(key);
			return KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(keyBytes));
		} catch (final InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RSAException("Fehler beim Dekodieren des privaten RSA-Schlüssels.", e);
		}
	}

}
