package de.svws_nrw.base.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

/**
 * Diese Klasse prüft die Methoden der {@link AES}-Klasse.  
 */
public class TestAES {

	/**
	 * Tests für die Verschlüsselung und Entschlüsselung von Strings mit dem AES-Verfahren
	 */
	@Test
	void testStringEncrpytion() {
		try {
			SecretKey key = AES.getKey256("Streng geheim", "Salz");
			AES aes = new AES(AESAlgo.CBC_PKCS5Padding, key);
			String original = "EineNachricht";
			String encoded = aes.encryptBase64(original.getBytes());
			String decoded = new String(aes.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (AESException e) {
			fail(e);
		}
	}
	
}
