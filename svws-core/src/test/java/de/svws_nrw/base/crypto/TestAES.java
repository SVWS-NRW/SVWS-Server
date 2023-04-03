package de.svws_nrw.base.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import de.svws_nrw.base.ResourceUtils;

/**
 * Diese Klasse prüft die Methoden der {@link AES}-Klasse.
 */
public class TestAES {

	private static final String PFAD_DATEN = "de/svws_nrw/base/crypto/";

	/**
	 * Tests für die Verschlüsselung und Entschlüsselung von Strings mit dem AES-Verfahren
	 */
	@Test
	void testStringEncrpytion() {
		try {
			final SecretKey key = AES.getKey256("Streng geheim", "Salz");
			final AES aes = new AES(AESAlgo.CBC_PKCS5Padding, key);
			final String original = "EineNachricht";
			final String encoded = aes.encryptBase64(original.getBytes());
			final String decoded = new String(aes.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (final AESException e) {
			fail(e);
		}
	}

	/**
	 * Tests für die Verschlüsselung und Entschlüsselung eine JSON-Datei mit dem AES-Verfahren
	 */
	@Test
	void testJSONFileEncrytion() {
		try {
			final SecretKey key = AES.getKey256("Streng geheim", "Salz");
			final AES aes = new AES(AESAlgo.CBC_PKCS5Padding, key);
			final Path path = ResourceUtils.getFile(PFAD_DATEN + "/enm.json");
			final String original = Files.readString(path);
			final String encoded = aes.encryptBase64(original.getBytes());
			final String decoded = new String(aes.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (final Exception e) {
			fail(e);
		}
	}

}
