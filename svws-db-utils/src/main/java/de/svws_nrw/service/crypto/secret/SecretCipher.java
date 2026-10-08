package de.svws_nrw.service.crypto.secret;

import java.nio.charset.StandardCharsets;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESAlgo;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ver- und entschlüsselt Secrets mit AES (CBC, PKCS5-Padding). Das Ergebnis der Verschlüsselung ist
 * Base64-kodiert und enthält den zufälligen Initialisierungsvektor (IV) vor dem eigentlichen Chiffrat.
 * Den Schlüssel liefert ein {@link SecretKeyProvider}; diese Klasse greift selbst nicht auf Dateien zu.
 */
public final class SecretCipher {

	private static final String FEHLER_VERSCHLUESSELN = "Das Secret konnte nicht verschlüsselt werden.";
	private static final String FEHLER_ENTSCHLUESSELN = "Das Secret konnte nicht entschlüsselt werden. Die Zugangsdaten müssen neu hinterlegt werden.";

	private final SecretKeyProvider keyProvider;


	/**
	 * Erstellt einen neuen Cipher.
	 *
	 * @param keyProvider   der Provider für den symmetrischen Schlüssel, Singleton per Factory
	 */
	public SecretCipher(final SecretKeyProvider keyProvider) {
		this.keyProvider = keyProvider;
	}

	/**
	 * Verschlüsselt das übergebene Secret.
	 *
	 * @param plain   das Secret im Klartext
	 *
	 * @return das verschlüsselte Secret als Base64-String (IV und Chiffrat)
	 *
	 * @throws ApiOperationException   (500) falls das Secret nicht verschlüsselt werden kann
	 */
	public String encrypt(final String plain) {
		try {
			return createAES().encryptBase64(plain.getBytes(StandardCharsets.UTF_8));
		} catch (final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, FEHLER_VERSCHLUESSELN);
		}
	}

	/**
	 * Entschlüsselt das übergebene Secret.
	 *
	 * @param cipherBase64   das verschlüsselte Secret als Base64-String (IV und Chiffrat)
	 *
	 * @return das Secret im Klartext
	 *
	 * @throws ApiOperationException   (500) falls das Secret nicht entschlüsselt werden kann
	 */
	public String decrypt(final String cipherBase64) {
		try {
			return new String(createAES().decryptBase64(cipherBase64), StandardCharsets.UTF_8);
		} catch (final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, FEHLER_ENTSCHLUESSELN);
		}
	}

	private AES createAES() {
		return new AES(AESAlgo.CBC_PKCS5PADDING, keyProvider.getKey());
	}

}
