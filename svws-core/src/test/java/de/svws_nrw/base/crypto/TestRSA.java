package de.svws_nrw.base.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.svws_nrw.base.ResourceUtils;

/**
 * Diese Klasse prüft die Methoden der {@link RSA}-Klasse.
 */
class TestRSA {

	private static final String PFAD_DATEN = "de/svws_nrw/base/crypto/";

	private static KeyPair keypair = null;

	/**
	 * Generiert ein Schlüsselpaar für die weiteren Tests
	 *
	 * @throws RSAException falls ein Fehler beim Erzeugen des Schlüsselpaares auftritt
	 */
	@BeforeAll
	static void setup() throws RSAException {
		keypair = RSA.createKey();
		assertNotNull(keypair);
	}


	/**
	 * Tests für die Verschlüsselung und Entschlüsselung von Strings mit dem AES-Verfahren
	 */
	@Test
	void testStringEncrpytion() {
		try {
			final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());
			final String original = "EineNachricht";
			final String encoded = rsa.encryptBase64(original.getBytes());
			final String decoded = new String(rsa.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (final RSAException e) {
			fail(e);
		}
	}

	/**
	 * Tests für die Verschlüsselung und Entschlüsselung eine JSON-Datei mit dem AES-Verfahren
	 */
	@Test
	void testJSONFileEncrytion() {
		try {
			final RSA rsa = new RSA(keypair.getPrivate(), keypair.getPublic());
			final Path path = ResourceUtils.getFile(PFAD_DATEN + "/enm.json");
			final String original = Files.readString(path);
			final String encoded = rsa.encryptBase64(original.getBytes());
			final String decoded = new String(rsa.decryptBase64(encoded));
			assertEquals(original, decoded);
		} catch (final Exception e) {
			fail(e);
		}
	}

}
