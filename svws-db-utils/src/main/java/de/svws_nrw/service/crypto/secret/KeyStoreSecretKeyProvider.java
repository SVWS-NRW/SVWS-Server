package de.svws_nrw.service.crypto.secret;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Optional;
import javax.crypto.SecretKey;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.crypto.KeyStoreUtils;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Stellt den symmetrischen Schlüssel für die Ver- und Entschlüsselung von Secrets aus einem dedizierten
 * Java-Keystore bereit. Existiert die Keystore-Datei oder der Eintrag unter dem Alias noch nicht, so wird ein
 * neuer AES-256-Schlüssel erzeugt und der Keystore atomar gespeichert. Eine vorhandene, aber nicht lesbare
 * Keystore-Datei wird niemals überschrieben. Der Schlüssel wird bewusst nicht im Speicher gehalten, sondern bei
 * jedem Zugriff neu aus dem Keystore geladen, um seine Verweildauer im Heap zu verkürzen.
 */
public final class KeyStoreSecretKeyProvider implements SecretKeyProvider {

	private static final String FEHLER_LADEN = "Der Schlüssel zur Verschlüsselung von Secrets konnte nicht aus dem Keystore geladen werden.";
	private static final String FEHLER_ERZEUGEN = "Der Schlüssel zur Verschlüsselung von Secrets konnte nicht erzeugt werden.";
	private static final String FEHLER_SPEICHERN = "Der Keystore für die Verschlüsselung von Secrets konnte nicht gespeichert werden.";

	private final Path keystoreFile;
	private final String password;
	private final String alias;


	/**
	 * Erstellt einen neuen Provider für die angegebene Keystore-Datei.
	 *
	 * @param keystoreFile   der Pfad der Keystore-Datei
	 * @param password       das Kennwort für den Keystore und den Eintrag
	 * @param alias          der Alias des Schlüssels im Keystore
	 */
	public KeyStoreSecretKeyProvider(final Path keystoreFile, final String password, final String alias) {
		this.keystoreFile = keystoreFile;
		this.password = password;
		this.alias = alias;
	}

	@Override
	public synchronized SecretKey getKey() {
		return loadOrCreateKey();
	}

	private SecretKey loadOrCreateKey() {
		final KeyStore keystore = loadOrCreateKeystore();

		return readKey(keystore).orElseGet(() -> createAndStoreKey(keystore));
	}

	private KeyStore loadOrCreateKeystore() {
		try {
			if (!Files.exists(keystoreFile)) {
				return KeyStoreUtils.newKeystore();
			}

			return KeyStoreUtils.getKeystore(keystoreFile.toString(), password);
		} catch (final KeyStoreException e) {
			throw createError(FEHLER_LADEN, e);
		}
	}

	private SecretKey createAndStoreKey(final KeyStore keystore) {
		final SecretKey neu = createKey();
		storeKey(keystore, neu);
		return neu;
	}

	private Optional<SecretKey> readKey(final KeyStore keystore) {
		try {
			return Optional.ofNullable(KeyStoreUtils.getSecretKey(keystore, alias, password));
		} catch (final KeyStoreException e) {
			throw createError(FEHLER_LADEN, e);
		}
	}

	private SecretKey createKey() {
		try {
			return AES.getRandomKey256();
		} catch (final AESException e) {
			throw createError(FEHLER_ERZEUGEN, e);
		}
	}

	private void storeKey(final KeyStore keystore, final SecretKey key) {
		try {
			KeyStoreUtils.setSecretKey(keystore, alias, key, password);
			KeyStoreUtils.storeKeystore(keystore, keystoreFile.toString(), password);
		} catch (final KeyStoreException e) {
			throw createError(FEHLER_SPEICHERN, e);
		}
	}

	private ApiOperationException createError(final String meldung, final Exception cause) {
		Logger.global().logLn(LogLevel.ERROR, meldung + " Keystore: " + keystoreFile.toAbsolutePath() + " - Ursache: " + cause.getMessage());
		return new ApiOperationException(Status.INTERNAL_SERVER_ERROR, cause, meldung);
	}

}
